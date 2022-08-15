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
import org.archguard.scanner.ctl.command.ScannerCommand
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Serializable
data class Project(val name: String, val language: String)

open class ArchGuardProtobufClient(
    private val language: String,
    private val serverUrl: String,
    private val systemId: String,
    private val path: String,
    private val command: ScannerCommand,
) : ArchGuardClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val client: HttpClient by lazy {
        HttpClient.newBuilder().connectTimeout(Duration.ofMinutes(2)).build()
    }

    private fun buildUrl(topic: String) =
        "$serverUrl/api/scanner/$systemId/reporting/$topic?language=$language&path=$path"

    private inline fun process(uri: URI, body: ByteArray) {
        val request = HttpRequest.newBuilder(uri).header("Content-Type", "application/x-protobuf")
            .POST(HttpRequest.BodyPublishers.ofByteArray(body)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        logger.info(
            """
                response status: ${response.statusCode()}
                response body: ${response.body()}
            """.trimIndent()
        )
    }

    private inline fun process(topic: String, body: ByteArray) {
        logger.info("process topic: $topic")
        process(URI(buildUrl(topic)), body)
    }

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        process("class-items", ProtoBuf.encodeToByteArray(codes))
    }

    override fun saveApi(apis: List<ContainerService>) {
        process("container-services", ProtoBuf.encodeToByteArray(apis))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        process("datamap-relations", ProtoBuf.encodeToByteArray(records))
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        process("git-logs", ProtoBuf.encodeToByteArray(gitLogs))
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        val url = buildUrl("diff-changes") + "&since=${command.since}" + "&until=${command.until}"
        process(URI(url), ProtoBuf.encodeToByteArray(calls))
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        process("sca-dependencies", ProtoBuf.encodeToByteArray(dependencies))
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        process("issues", ProtoBuf.encodeToByteArray(issues))
    }

}