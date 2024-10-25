package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.context.LanguageEstimate
import org.archguard.context.ChangedCall
import org.archguard.context.GitLogs
import org.archguard.context.ApiCollection
import org.archguard.context.CompositionDependency
import org.archguard.context.CodeDatabaseRelation
import org.archguard.context.ContainerService
import java.io.File

open class ArchGuardJsonClient(private val systemId: String) : ArchGuardClient {
    private fun buildFileName(topic: String): String = systemId + "_" + topic + ".json"
    private inline fun <reified T> writeJsonFile(data: T, topic: String) {
        File(buildFileName(topic)).writeText(Json.encodeToString(data))
    }

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        writeJsonFile(codes, "codes")
    }

    override fun saveApi(apis: List<ContainerService>) {
        writeJsonFile(apis, "apis")
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        writeJsonFile(records, "databases")
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        if (gitLogs.isEmpty()) {
            return
        }

        writeJsonFile(gitLogs[0].commitLog, "gitlogs-commit")
        writeJsonFile(gitLogs[0].changeEntry, "gitlogs-change-entry")
        writeJsonFile(gitLogs[0].pathChangeCount, "gitlogs-change-count")
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        writeJsonFile(calls, "diff-changes")
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        writeJsonFile(dependencies, "sca-dependencies")
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        writeJsonFile(issues, "issues")
    }

    override fun saveEstimates(estimates: List<LanguageEstimate>) {
        writeJsonFile(estimates, "estimates")
    }

    override fun saveOpenApi(collections: List<ApiCollection>) {
        writeJsonFile(collections, "openapi")
    }
}
