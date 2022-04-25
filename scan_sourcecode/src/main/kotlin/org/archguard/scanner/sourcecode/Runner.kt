package org.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.thoughtworks.archguard.infrastructure.DBIStore
import com.thoughtworks.archguard.infrastructure.SourceBatch.ALL_TABLES
import com.thoughtworks.archguard.infrastructure.task.SqlExecuteThreadPool
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.analyser.ApiCallAnalyser
import org.archguard.scanner.analyser.CSharpAnalyser
import org.archguard.scanner.analyser.GoAnalyser
import org.archguard.scanner.analyser.JavaAnalyser
import org.archguard.scanner.analyser.KotlinAnalyser
import org.archguard.scanner.analyser.PythonAnalyser
import org.archguard.scanner.analyser.TypeScriptAnalyser
import org.archguard.scanner.common.DatamapRepository
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.sourcecode.database.MysqlAnalyser
import org.archguard.scanner.sourcecode.xml.XmlParser
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.Phaser
import kotlin.io.path.Path
import kotlin.io.path.exists

class Runner : CliktCommand(help = "scan git to sql") {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val store = DBIStore.getInstance()

    private val path: String by option(help = "local path").default(".")
    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "langauge: Java, Kotlin, TypeScript, CSharp, Python, Golang").default(
        "Java"
    )
    private val withoutStorage: Boolean by option(help = "skip storage").flag(default = false)

    // TODO: temporary solution
    private val context: SourceCodeContext by lazy {
        PluggableScannerAdapter.buildContext(language.lowercase(), path, withoutStorage, systemId)
    }

    override fun run() {
        cleanSqlFile(ALL_TABLES)

        val dataStructs = when (context.language) {
            "java" ->
                JavaAnalyser(context).analyse()
            "kotlin" ->
                KotlinAnalyser(context).analyse()
            "typescript", "javascript" ->
                TypeScriptAnalyser(context).analyse()
            "csharp", "c#" ->
                CSharpAnalyser(context).analyse()
            "python" ->
                PythonAnalyser(context).analyse()
            "go" ->
                GoAnalyser(context).analyse()
            else -> throw IllegalArgumentException("Unknown language: ${context.language}")
        }

        ApiCallAnalyser(context).analyse(dataStructs)

        // save json, save api have been moved to analysers

        saveDatabaseRelations(dataStructs, systemId, context.language)

        logger.info("start insert data into Mysql")
        val sqlStart = System.currentTimeMillis()

        storeDatabase(ALL_TABLES, systemId)

        val sqlEnd = System.currentTimeMillis()
        logger.info("Insert into MySql spend: {} s", (sqlEnd - sqlStart) / 1000)
        SqlExecuteThreadPool.close()
    }

    private fun saveDatabaseRelations(dataStructs: List<CodeDataStruct>, systemId: String, language: String) {
        when (language.lowercase()) {
            "java", "kotlin" -> {
                logger.info("start analysis database api ---- ${language.lowercase()}")

                val sqlAnalyser = MysqlAnalyser()
                val records = dataStructs.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }.toList()

                val mybatisEntries = XmlParser.parseMybatis(path)
                val relations = sqlAnalyser.convertMyBatis(mybatisEntries)

                relations.addAll(records)

                val repo = DatamapRepository(systemId, language, path)
                repo.saveRelations(relations)
                repo.close()

                File("database.json").writeText(Json.encodeToString(relations))
            }
        }
    }

    private fun storeDatabase(tables: Array<String>, systemId: String) {
        if (withoutStorage) return

        store.disableForeignCheck()
        store.initConnectionPool()
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
        store.enableForeignCheck()
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

fun main(args: Array<String>) = Runner().main(args)

