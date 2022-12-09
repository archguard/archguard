package com.thoughtworks.archguard.smartscanner.controller

import chapi.domain.core.CodeDataStruct
import com.thoughtworks.archguard.infrastructure.DBIStore
import com.thoughtworks.archguard.smartscanner.repository.BatchClassRepository
import com.thoughtworks.archguard.smartscanner.repository.ContainerRepository
import com.thoughtworks.archguard.smartscanner.repository.DatamapRepository
import com.thoughtworks.archguard.smartscanner.repository.DiffChangesRepository
import com.thoughtworks.archguard.smartscanner.repository.GitSourceRepository
import com.thoughtworks.archguard.smartscanner.repository.IssueDto
import com.thoughtworks.archguard.smartscanner.repository.IssueRepository
import com.thoughtworks.archguard.smartscanner.repository.ScaRepository
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Phaser
import kotlin.io.path.Path
import kotlin.io.path.exists

@Service
class ScannerReportingService(
    @Value("\${spring.datasource.url}") val dbUrl: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String,
    @Value("\${client.host}") val url: String,
    private val gitSourceRepository: GitSourceRepository,
    private val diffChangesRepository: DiffChangesRepository,
    private val scaRepository: ScaRepository,
    @Autowired private val issueRepository: IssueRepository,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val store: DBIStore by lazy { DBIStore.getInstance() }

    // FIXME: temporary solution to migrate the data persis
    init {
        System.setProperty("dburl", dbUrl.replace("://", "://$username:$password@"))
    }

    fun processClassItems(
        systemId: String,
        language: String,
        path: String,
        input: List<CodeDataStruct>,
    ) {
        val tables = arrayOf(
            "code_class",
            "code_field",
            "code_method",
            "code_annotation",
            "code_annotation_value",
            "code_ref_class_fields",
            "code_ref_class_methods",
            "code_ref_class_parent",
            "code_ref_method_fields",
            "code_ref_method_callees",
            "code_ref_class_dependencies",
        )

        try {
            val repo = BatchClassRepository(systemId, language, path)
            input.forEach { data -> repo.saveClassItem(data) }
            input.forEach { data -> repo.saveClassBody(data) }
            execute(systemId, tables)

            // todo: post
            val params = HashMap<String, Any>()
            params["systemId"] = systemId
            try {
                RestTemplate().postForLocation(
                    "$url/api/scanner/systems/{systemId}/dependency-analyses/post-analyse",
                    null,
                    params
                )
            } catch (ex: Exception) {
                throw ex
            }

        } finally {
            cleanSqlFile(tables)
        }
    }


    fun processContainerServices(
        systemId: String,
        language: String,
        path: String,
        input: List<ContainerService>,
    ) {
        val tables = arrayOf(
            "container_demand",
            "container_resource",
            "container_service",
        )

        try {
            val repo = ContainerRepository(systemId, language, path)
            repo.saveContainerServices(input.toTypedArray())
            execute(systemId, tables)
        } finally {
            cleanSqlFile(tables)
        }
    }

    fun processDatamapRelations(
        systemId: String,
        language: String,
        path: String,
        input: List<CodeDatabaseRelation>,
    ) {
        val tables = arrayOf(
            "data_code_database_relation",
        )

        try {
            val repo = DatamapRepository(systemId, language, path)
            repo.saveRelations(input)
            execute(systemId, tables)
        } finally {
            cleanSqlFile(tables)
        }
    }


    fun processIssues(input: List<Issue>, systemId: Long) {
        if (input.isNotEmpty()) {
            issueRepository.saveIssues(systemId, input.map {
                IssueDto.from(it)
            }, input[0].ruleType.toString())
        }
    }

    fun processGitLogs(inputs: List<GitLogs>, systemId: Long) {
        if (inputs.isNotEmpty()) {
            gitSourceRepository.saveGitReport(systemId, inputs[0])
        }
    }

    fun processScaDependencies(
        systemId: Long,
        input: List<CompositionDependency>,
    ) {
        scaRepository.saveDependencies(systemId, input)
    }

    fun processDiffChanges(
        systemId: Long,
        since: String,
        until: String,
        input: List<ChangedCall>,
    ) {
        diffChangesRepository.saveDiffs(systemId, since, until, input)
    }

    // TODO refactor: use direct sql or dao to insert the data
    private fun execute(systemId: String, tables: Array<String>) {
        // store.disableForeignCheck()
        // store.initConnectionPool()
        logger.info("========================================================")

        val phaser = Phaser(1)
        deleteByTables(tables, phaser, systemId)
        phaser.arriveAndAwaitAdvance()
        logger.info("============ system {} clean db is done ==============", systemId)
        saveByTables(tables, phaser)
        phaser.arriveAndAwaitAdvance()
        logger.info("============ system {} insert db is done ==============", systemId)
        updateByTables(tables, phaser)
        phaser.arriveAndAwaitAdvance()
        logger.info("============ system {} update db is done ==============", systemId)
        logger.info("========================================================")
        // store.enableForeignCheck()
    }

    private fun deleteByTables(tables: Array<String>, phaser: Phaser, systemId: String) {
        for (table in tables) {
            val dStart = System.currentTimeMillis()
            store.delete(table, phaser, systemId)
            val dEnd = System.currentTimeMillis()
            logger.info("delete {} spend {}", table, (dEnd - dStart) / 1000)
        }
    }

    private fun saveByTables(tables: Array<String>, phaser: Phaser) {
        for (table in tables) {
            val sStart = System.currentTimeMillis()
            val sqlPath = Path("$table.sql")
            if (!sqlPath.exists()) {
                continue
            }

            val sqls: List<String> = Files.readAllLines(sqlPath)
            if (sqls.isNotEmpty()) {
                store.save(sqls, table, phaser)
                val sEnd = System.currentTimeMillis()
                logger.info(
                    "save table {} with records {} spend {}",
                    table,
                    sqls.size,
                    (sEnd - sStart) / 1000
                )
            }
        }
    }

    private fun updateByTables(tables: Array<String>, phaser: Phaser) {
        for (table in tables) {
            val sStart = System.currentTimeMillis()
            val sqlPath = Path("$table.update.sql")
            if (!sqlPath.exists()) {
                continue
            }

            val sqls: List<String> = Files.readAllLines(sqlPath)
            if (sqls.isNotEmpty()) {
                store.save(sqls, table, phaser)
                val sEnd = System.currentTimeMillis()
                logger.info(
                    "update table {} with records {} spend {}",
                    table,
                    sqls.size,
                    (sEnd - sStart) / 1000
                )
            }
        }
    }

    private fun cleanSqlFile(tables: Array<String>) {
        cleanInsertSqlFile(tables)
        cleanUpdateSqlFile(tables)
        // SqlExecuteThreadPool.close()
    }

    private fun cleanInsertSqlFile(tables: Array<String>) {
        for (table in tables) {
            cleanFile("$table.sql")
        }
    }

    private fun cleanUpdateSqlFile(tables: Array<String>) {
        for (table in tables) {
            cleanFile("$table.update.sql")
        }
    }

    private fun cleanFile(fileName: String) {
        val file = Paths.get(fileName)
        if (file.toFile().exists()) {
            logger.info("clean {}", fileName)
            try {
                Files.delete(Path(fileName))
            } catch (e: IOException) {
                logger.error("delete {} failed", fileName)
            }
        }
    }
}
