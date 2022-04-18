package org.archguard.scanner.core.context

import chapi.domain.core.CodeDataStruct

// client of the archguard, scanner can communicate to server via this client with limited functions
interface ArchGuardClient {
    fun saveDataStructure(dataStructure: List<CodeDataStruct>, systemId: String, language: String)
    fun saveApi(api: List<ContainerService>, systemId: String, language: String)
}
