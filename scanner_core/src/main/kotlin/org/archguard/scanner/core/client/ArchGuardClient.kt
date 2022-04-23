package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.dto.ContainerService

// client of the archguard, scanner can communicate to server via this client with limited functions
interface ArchGuardClient {
    // File("structs.json").writeText(Json.encodeToString(dataStructs))
    fun saveDataStructure(dataStructure: List<CodeDataStruct>)

    // File("apis.json").writeText(Json.encodeToString(apiCalls))
    fun saveApi(api: List<ContainerService>)
}
