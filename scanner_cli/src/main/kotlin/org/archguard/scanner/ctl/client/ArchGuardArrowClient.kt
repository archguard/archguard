package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.estimate.LanguageEstimate
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.writeArrowFeather
import java.io.File

class ArchGuardArrowClient(private val systemId: String) : ArchGuardClient {
    private fun filename(topic: String): String = systemId + "_" + topic + ".arrow"
    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        codes.toDataFrame().writeArrowFeather(File(filename("codes")))
    }

    override fun saveApi(apis: List<ContainerService>) {
        apis.toDataFrame().writeArrowFeather(File(filename("apis")))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        records.toDataFrame().writeArrowFeather(File(filename("databases")))
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        gitLogs[0].commitLog.toDataFrame().writeArrowFeather(File(filename("gitlog-commit")))
        gitLogs[0].changeEntry.toDataFrame().writeArrowFeather(File(filename("gitlog-change-entry")))
        gitLogs[0].pathChangeCount.toDataFrame().writeArrowFeather(File(filename("gitlog-change-count")))
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        calls.toDataFrame().writeArrowFeather(File(filename("diff-changes")))
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        dependencies.toDataFrame().writeArrowFeather(File(filename("sca-dependencies")))
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        issues.toDataFrame().writeArrowFeather(File(filename("issues")))
    }

    override fun saveEstimates(estimates: List<LanguageEstimate>) {
        estimates.toDataFrame().writeArrowFeather(File(filename("estimates")))
    }
}