package com.thoughtworks.archguard.scanner.sourcecode

import chapi.app.analyser.*
import chapi.app.analyser.config.ChapiConfig
import chapi.domain.core.CodeDataStruct
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.thoughtworks.archguard.scanner.sourcecode.frontend.FrontendApiAnalyser
import infrastructure.DBIStore
import infrastructure.SourceBatch.TABLES
import infrastructure.task.SqlExecuteThreadPool
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    private val withApi: Boolean by option(help = "with container service api").flag()
    private val language: String by option(help = "langauge: Java, Kotlin, TypeScript, CSharp, Python, Golang").default(
        "Java"
    )

    override fun run() {
        cleanSqlFile(TABLES)

        var dataStructs: Array<CodeDataStruct> = arrayOf()
        val lang = language.lowercase()
        when (lang) {
            "java" -> {
                dataStructs = JavaAnalyserApp().analysisNodeByPath(path)
            }
            "kotlin" -> {
                dataStructs = KotlinAnalyserApp().analysisNodeByPath(path)
            }
            "typescript", "javascript" -> {
                dataStructs = TypeScriptAnalyserApp().analysisNodeByPath(path)
            }
            "csharp", "c#" -> {
                dataStructs = CSharpAnalyserApp().analysisNodeByPath(path)
            }
            "python" -> {
                dataStructs = PythonAnalyserApp(ChapiConfig()).analysisNodeByPath(path)
            }
            "go" -> {
                dataStructs = GoAnalyserApp(ChapiConfig()).analysisNodeByPath(path)
            }
        }

        File("structs.json").writeText(Json.encodeToString(dataStructs))
        toSql(dataStructs, systemId, lang)

        logger.info("start insert data into Mysql")
        val sqlStart = System.currentTimeMillis()
        storeDatabase(TABLES, systemId)
        val sqlEnd = System.currentTimeMillis()
        logger.info("Insert into MySql spend: {} s", (sqlEnd - sqlStart) / 1000)
        SqlExecuteThreadPool.close()
    }

    private fun toSql(dataStructs: Array<CodeDataStruct>, systemId: String, language: String) {
        val repo = ClassRepository(systemId, language, path)
//
//        if (withApi) {
//            when (language.lowercase()) {
//                "typescript", "javascript" -> {
//
//                }
//            }
//        }

        val feApiAnalyser = FrontendApiAnalyser()
        // save class first, and can query dependencies for later
        dataStructs.forEach { data ->
            repo.saveClassItem(data)
            feApiAnalyser.analysisByNode(data, path)
        }

        // save class imports, callees and parent
        dataStructs.forEach { data ->
            repo.saveClassBody(data)
        }

        logger.info("========================================================")
        logger.info("start analysis frontend api")
        val apiCalls = feApiAnalyser.toContainerServices()
        val containerRepository = ContainerRepository(systemId, language, path)
        File("apis.json").writeText(Json.encodeToString(apiCalls))

        containerRepository.saveContainerServices(apiCalls)
        containerRepository.close()
        logger.info("========================================================")

        repo.close()
    }

    private fun storeDatabase(tables: Array<String>, systemId: String) {
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

