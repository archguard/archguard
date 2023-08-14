package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.openapi.ApiCollection
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService

class ChainedArchGuardClient(
    private val clients: List<ArchGuardClient>,
) : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) = clients.forEach { it.saveDataStructure(codes) }
    override fun saveApi(apis: List<ContainerService>) = clients.forEach { it.saveApi(apis) }
    override fun saveRelation(records: List<CodeDatabaseRelation>) = clients.forEach { it.saveRelation(records) }
    override fun saveGitLogs(gitLogs: List<GitLogs>) = clients.forEach { it.saveGitLogs(gitLogs) }
    override fun saveDiffs(calls: List<ChangedCall>) = clients.forEach { it.saveDiffs(calls) }
    override fun saveDependencies(dependencies: List<CompositionDependency>) =
        clients.forEach { it.saveDependencies(dependencies) }

    override fun saveRuleIssues(issues: List<Issue>) {
        clients.forEach { it.saveRuleIssues(issues) }
    }

    override fun saveEstimates(estimates: List<LanguageEstimate>) {
        clients.forEach { it.saveEstimates(estimates) }
    }

    override fun saveOpenApi(collections: List<ApiCollection>) {
        clients.forEach { it.saveOpenApi(collections) }
    }
}
