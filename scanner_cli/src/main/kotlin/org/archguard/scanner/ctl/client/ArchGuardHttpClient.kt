package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

// 通过http api回写分析数据
class ArchGuardHttpClient(language: String, serverUrl: String, systemId: String, path: String) : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        TODO("Not yet implemented")
    }

    override fun saveApi(apis: List<ContainerService>) {
        TODO("Not yet implemented")
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        TODO("Not yet implemented")
    }
}
