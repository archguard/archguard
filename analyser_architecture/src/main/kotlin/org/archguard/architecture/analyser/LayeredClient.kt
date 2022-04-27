package org.archguard.architecture.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

class LayeredClient : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) {}

    override fun saveApi(apis: List<ContainerService>) {}

    override fun saveRelation(records: List<CodeDatabaseRelation>) {}
}
