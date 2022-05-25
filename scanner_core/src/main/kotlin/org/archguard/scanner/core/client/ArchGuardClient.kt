package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService

// api of the archguard, scanner can communicate to server via this api with limited functions
interface ArchGuardClient {
    fun saveDataStructure(codes: List<CodeDataStruct>)
    fun saveApi(apis: List<ContainerService>)
    fun saveRelation(records: List<CodeDatabaseRelation>)
    fun saveGitLogs(gitLogs: GitLogs)
    fun saveDiffs(calls: List<ChangedCall>)
    fun saveDependencies(dependencies: List<CompositionDependency>)
}
