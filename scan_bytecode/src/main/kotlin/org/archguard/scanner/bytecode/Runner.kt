package org.archguard.scanner.bytecode

import chapi.domain.core.CodeDataStruct
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import infrastructure.DBIStore
import infrastructure.SourceBatch.ALL_TABLES
import infrastructure.task.SqlExecuteThreadPool
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.ClassRepository
import org.archguard.scanner.common.ContainerRepository
import org.archguard.scanner.common.backend.JavaApiAnalyser
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
    private val apiOnly: Boolean by option(help = "only scan api").flag()
    private val systemId: String by option(help = "system id").default("0")
    private val language: String by option(help = "langauge: Java, Kotlin, TypeScript, CSharp, Python, Golang").default("Jvm")


    override fun run() {
        cleanSqlFile(ALL_TABLES)

        var dataStructs: Array<CodeDataStruct> = arrayOf()

        storage(dataStructs)
    }

    private fun storage(dataStructs: Array<CodeDataStruct>) {
        val lang = language.lowercase()
        saveDataStructs(dataStructs, systemId, lang)

        saveApi(dataStructs, systemId, lang)

        logger.info("start insert data into Mysql")
        val sqlStart = System.currentTimeMillis()

        storeDatabase(ALL_TABLES, systemId)

        val sqlEnd = System.currentTimeMillis()
        logger.info("Insert into MySql spend: {} s", (sqlEnd - sqlStart) / 1000)
        SqlExecuteThreadPool.close()
    }

    private fun saveDataStructs(dataStructs: Array<CodeDataStruct>, systemId: String, language: String) {
        val repo = ClassRepository(systemId, language, path)

        dataStructs.forEach { data ->
            repo.saveClassItem(data)
        }

        // save class imports, callees and parent
        dataStructs.forEach { data ->
            repo.saveClassBody(data)
        }

        repo.close()
    }

    private fun saveApi(dataStructs: Array<CodeDataStruct>, systemId: String, language: String) {
        logger.info("========================================================")
        when (language.lowercase()) {
            "java", "kotlin" -> {
                logger.info("start analysis backend api ---- ${language.lowercase()}")

                val apiAnalyser = JavaApiAnalyser()
                dataStructs.forEach { data ->
                    apiAnalyser.analysisByNode(data, "")
                }

                val apiCalls = apiAnalyser.toContainerServices()

                val containerRepository = ContainerRepository(systemId, language, path)
                File("apis.json").writeText(Json.encodeToString(apiCalls))
                containerRepository.saveContainerServices(apiCalls)
                containerRepository.close()
            }
        }
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

