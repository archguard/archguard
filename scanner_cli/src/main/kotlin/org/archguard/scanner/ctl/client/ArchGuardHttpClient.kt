package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.context.ArchGuardClient
import org.archguard.scanner.core.context.ContainerService

// 通过http api回写分析数据
class ArchGuardHttpClient(baseUrl: String) : ArchGuardClient {
    override fun saveDataStructure(dataStructure: List<CodeDataStruct>, systemId: String, language: String) {
        TODO("Not yet implemented")
    }

    override fun saveApi(api: List<ContainerService>, systemId: String, language: String) {
        TODO("Not yet implemented")
    }
}
