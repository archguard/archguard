package org.archguard.scanner.core.client

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
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
    fun saveGitLogs(gitLogs: List<GitLogs>)
    fun saveDiffs(calls: List<ChangedCall>)
    fun saveDependencies(dependencies: List<CompositionDependency>)
    fun saveRuleIssues(issues: List<Issue>)
}

class EmptyArchGuardClient: ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) {

    }

    override fun saveApi(apis: List<ContainerService>) {

    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {

    }
    override fun saveGitLogs(gitLogs: List<GitLogs>) {

    }

    override fun saveDiffs(calls: List<ChangedCall>) {

    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {

    }

    override fun saveRuleIssues(issues: List<Issue>) {

    }
}