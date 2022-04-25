package org.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.ClassRepository
import org.archguard.scanner.common.ContainerRepository
import org.archguard.scanner.common.DatamapRepository
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory
import java.io.File

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

        override fun saveDataStructure(codes: List<CodeDataStruct>) {
            File("structs.json").writeText(Json.encodeToString(codes))

            codes.forEach { data ->
                repo.saveClassItem(data)
            }

            // fix for different order in class and depClass
            repo.flush()

            // save class imports, callees and parent
            codes.forEach { data ->
                repo.saveClassBody(data)
            }

            repo.close()
        }

        override fun saveApi(apis: List<ContainerService>) {
            File("apis.json").writeText(Json.encodeToString(apis))

            val containerRepository = ContainerRepository(systemId, language, path)
            containerRepository.saveContainerServices(apis.toTypedArray())
            containerRepository.close()
        }

        override fun saveRelation(records: List<CodeDatabaseRelation>) {
            File("database.json").writeText(Json.encodeToString(records))

            val repo = DatamapRepository(systemId, language, path)
            repo.saveRelations(records)
            repo.close()
        }
    }
}


