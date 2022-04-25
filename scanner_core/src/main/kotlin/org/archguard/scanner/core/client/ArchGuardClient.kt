package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

// client of the archguard, scanner can communicate to server via this client with limited functions
interface ArchGuardClient {
    // File("codes.json").writeText(Json.encodeToString(dataStructs))
    fun saveDataStructure(codes: List<CodeDataStruct>)

    // File("apis.json").writeText(Json.encodeToString(apiCalls))
    fun saveApi(apis: List<ContainerService>)

    // File("databases.json").writeText(Json.encodeToString(dataStructs))
    fun saveRelation(records: List<CodeDatabaseRelation>)
}
