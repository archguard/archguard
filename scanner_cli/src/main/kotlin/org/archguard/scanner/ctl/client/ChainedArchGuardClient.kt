package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.ctl.command.ScannerCommand

class ChainedArchGuardClient(
    private val clients: List<ArchGuardClient>,
) : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) = clients.forEach { it.saveDataStructure(codes) }
    override fun saveApi(apis: List<ContainerService>) = clients.forEach { it.saveApi(apis) }
    override fun saveRelation(records: List<CodeDatabaseRelation>) = clients.forEach { it.saveRelation(records) }
    override fun saveGitLogs(gitLogs: GitLogs) = clients.forEach { it.saveGitLogs(gitLogs) }
    override fun saveDiffs(calls: List<ChangedCall>) = clients.forEach { it.saveDiffs(calls) }
    override fun saveDependencies(dependencies: List<CompositionDependency>) =
        clients.forEach { it.saveDependencies(dependencies) }
}
