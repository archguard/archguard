package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService
import org.archguard.scanner.core.client.dto.GitLogs

// client of the archguard, scanner can communicate to server via this client with limited functions
interface ArchGuardClient {
    fun saveDataStructure(codes: List<CodeDataStruct>)
    fun saveApi(apis: List<ContainerService>)
    fun saveRelation(records: List<CodeDatabaseRelation>)
    fun saveGitLogs(gitLogs: GitLogs)
}
