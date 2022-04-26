package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

open class ArchGuardConsoleClient(private val systemId: String) : ArchGuardClient {
    private fun buildFileName(topic: String): String = systemId + "_" + topic + ".json"

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        println("==============================================================")
        println("Output code data structures to console")
        println(Json.encodeToString(codes))
    }

    override fun saveApi(apis: List<ContainerService>) {
        println("==============================================================")
        println("Output api container services to console")
        println(Json.encodeToString(apis))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        println("==============================================================")
        println("Output database relationships to console")
        println(Json.encodeToString(records))
    }
}
