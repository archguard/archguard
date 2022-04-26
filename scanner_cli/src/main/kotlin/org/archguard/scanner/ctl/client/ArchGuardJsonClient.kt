package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService
import java.io.File

open class ArchGuardJsonClient(private val systemId: String) : ArchGuardClient {
    private fun buildFileName(topic: String): String = systemId + "_" + topic + ".json"

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        File(buildFileName("codes")).writeText(Json.encodeToString(codes))
    }

    override fun saveApi(apis: List<ContainerService>) {
        File(buildFileName("apis")).writeText(Json.encodeToString(apis))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        File(buildFileName("databases")).writeText(Json.encodeToString(records))
    }
}
