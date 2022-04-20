package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.ContainerService

// 通过db insert回写分析数据
class ArchGuardDBClient : ArchGuardClient {
    override fun saveDataStructure(dataStructure: List<CodeDataStruct>, systemId: String, language: String) {
        TODO("Not yet implemented")
    }

    override fun saveApi(api: List<ContainerService>, systemId: String, language: String) {
        TODO("Not yet implemented")
    }
}
