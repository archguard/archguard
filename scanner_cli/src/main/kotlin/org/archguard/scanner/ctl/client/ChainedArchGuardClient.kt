package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

class ChainedArchGuardClient(
    private val clients: List<ArchGuardClient>,
) : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) = clients.forEach { it.saveDataStructure(codes) }
    override fun saveApi(apis: List<ContainerService>) = clients.forEach { it.saveApi(apis) }
    override fun saveRelation(records: List<CodeDatabaseRelation>) = clients.forEach { it.saveRelation(records) }
}

