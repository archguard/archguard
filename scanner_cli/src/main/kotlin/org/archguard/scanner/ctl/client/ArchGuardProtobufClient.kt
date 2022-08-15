package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import java.io.File

@Serializable
data class Project(val name: String, val language: String)

open class ArchGuardProtobufClient(private val systemId: String) : ArchGuardClient {
    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        val bytes = ProtoBuf.encodeToByteArray(codes)
        File("ds.proto").writeBytes(bytes);
    }

    override fun saveApi(apis: List<ContainerService>) {
        val bytes = ProtoBuf.encodeToByteArray(apis)
        File("api.proto").writeBytes(bytes);
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        val bytes = ProtoBuf.encodeToByteArray(records)
        File("relation.proto").writeBytes(bytes);
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        val bytes = ProtoBuf.encodeToByteArray(gitLogs)
        File("gitlogs.proto").writeBytes(bytes);
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        val bytes = ProtoBuf.encodeToByteArray(calls)
        File("diffs.proto").writeBytes(bytes);
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        val bytes = ProtoBuf.encodeToByteArray(dependencies)
        File("dependencies.proto").writeBytes(bytes);
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        val bytes = ProtoBuf.encodeToByteArray(issues)
        File("issues.proto").writeBytes(bytes);
    }

}