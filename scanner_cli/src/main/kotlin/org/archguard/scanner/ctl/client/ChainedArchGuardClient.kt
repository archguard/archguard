package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.model.LanguageEstimate
import org.archguard.model.ChangedCall
import org.archguard.model.GitLogs
import org.archguard.model.ApiCollection
import org.archguard.model.CompositionDependency
import org.archguard.model.CodeDatabaseRelation
import org.archguard.model.ContainerService

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
