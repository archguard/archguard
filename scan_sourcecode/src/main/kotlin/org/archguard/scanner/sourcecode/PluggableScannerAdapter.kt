package org.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.ClassRepository
import org.archguard.scanner.common.ContainerRepository
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.ContainerService
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileWriter

object PluggableScannerAdapter {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun buildContext(
        l: String, p: String, s: Boolean,
        systemId: String,
    ) = object : SourceCodeContext {
        override val language: String get() = l
        override val features: List<String> get() = emptyList()
        override val path: String get() = p
        override val withoutStorage: Boolean get() = s
        override val client: ArchGuardClient get() = buildClient(systemId, l, p)
    }

    fun buildClient(systemId: String, l: String, p: String) = object : ArchGuardClient {
        private val language = l
        private val path = p
        private val repo = ClassRepository(systemId, l, p)

        override fun saveDataStructure(dataStructure: List<CodeDataStruct>) {
            File("structs.json").writeText(Json.encodeToString(dataStructure))

            dataStructure.forEach { data ->
                repo.saveClassItem(data)
            }

            // fix for different order in class and depClass
            repo.flush()

            // save class imports, callees and parent
            dataStructure.forEach { data ->
                repo.saveClassBody(data)
            }

            repo.close()
        }

        override fun saveApi(api: List<ContainerService>) {
            logger.info("========================================================")
            val containerRepository = ContainerRepository(systemId, language, path)
            writeCsvFile(api.toTypedArray(), "apis.csv")
            File("apis.json").writeText(Json.encodeToString(api))

            containerRepository.saveContainerServices(api.toTypedArray())
            containerRepository.close()
        }
    }

    val csvMapper = CsvMapper().apply {
        registerModule(KotlinModule())
    }

    inline fun <reified T> writeCsvFile(data: Array<T>, fileName: String) {
        FileWriter(fileName).use { writer ->
            csvMapper.writer(csvMapper.schemaFor(T::class.java).withHeader())
                .writeValues(writer)
                .writeAll(data)
                .close()
        }
    }
}


