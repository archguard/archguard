package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
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

    override fun saveGitLogs(gitLogs: GitLogs) {
        writeJsonFile(gitLogs.commitLog, "gitlogs-commit")
        writeJsonFile(gitLogs.changeEntry, "gitlogs-change-entry")
        writeJsonFile(gitLogs.pathChangeCount, "gitlogs-change-count")
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        writeJsonFile(calls, "diff-changes")
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        writeJsonFile(dependencies, "sca-dependencies")
    }
}
