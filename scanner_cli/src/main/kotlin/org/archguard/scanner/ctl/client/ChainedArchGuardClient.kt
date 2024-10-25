package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.architecture.ArchitectureView
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.context.LanguageEstimate
import org.archguard.context.ChangedCall
import org.archguard.context.GitLogs
import org.archguard.context.ApiCollection
import org.archguard.context.CompositionDependency
import org.archguard.context.CodeDatabaseRelation
import org.archguard.context.ContainerService

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

    override fun saveArchitecture(listOf: List<ArchitectureView>) {
        clients.forEach { it.saveArchitecture(listOf) }
    }
}
