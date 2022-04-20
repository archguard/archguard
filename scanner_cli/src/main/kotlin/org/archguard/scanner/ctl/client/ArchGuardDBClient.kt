package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.ContainerService

// 通过db insert回写分析数据
class ArchGuardDBClient(language: String, systemId: String, path: String) : ArchGuardClient {
    override fun saveDataStructure(dataStructure: List<CodeDataStruct>) {
        TODO("Not yet implemented")
    }

    override fun saveApi(api: List<ContainerService>) {
        TODO("Not yet implemented")
    }
}
