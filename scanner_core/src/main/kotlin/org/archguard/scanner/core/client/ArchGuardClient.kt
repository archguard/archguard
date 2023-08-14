package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.openapi.ApiCollection
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService

/**
 * api of the archguard, scanner can communicate to server via this api with limited functions
 */
interface ArchGuardClient {
    fun saveDataStructure(codes: List<CodeDataStruct>)
    fun saveApi(apis: List<ContainerService>)
    fun saveRelation(records: List<CodeDatabaseRelation>)
    fun saveGitLogs(gitLogs: List<GitLogs>)
    fun saveDiffs(calls: List<ChangedCall>)
    fun saveDependencies(dependencies: List<CompositionDependency>)

    /**
     * Lint issues to the server
     */
    fun saveRuleIssues(issues: List<Issue>)

    /**
     * Count of Lines with complexity
     */
    fun saveEstimates(estimates: List<LanguageEstimate>)
    fun saveOpenApi(collections: List<ApiCollection>)
}

class EmptyArchGuardClient : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) = Unit
    override fun saveApi(apis: List<ContainerService>) = Unit
    override fun saveRelation(records: List<CodeDatabaseRelation>) = Unit
    override fun saveGitLogs(gitLogs: List<GitLogs>) = Unit
    override fun saveDiffs(calls: List<ChangedCall>) = Unit
    override fun saveDependencies(dependencies: List<CompositionDependency>) = Unit
    override fun saveRuleIssues(issues: List<Issue>) = Unit
    override fun saveEstimates(estimates: List<LanguageEstimate>) = Unit
    override fun saveOpenApi(collections: List<ApiCollection>) = Unit
}