package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpResponse
import java.time.Duration

// 通过http api回写分析数据
class ArchGuardHttpClient(
    private val language: String,
    private val serverUrl: String,
    private val systemId: String,
    private val path: String,
    private val command: ScannerCommand,
) : ArchGuardClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val client: HttpClient by lazy {
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(2))
            .build()
    }

    private fun buildUrl(topic: String) =
        "$serverUrl/api/scanner/$systemId/reporting/$topic?language=$language&path=$path"

    private inline fun <reified T> process(uri: URI, body: T) {
        val request = HttpRequest.newBuilder(uri)
            .POST(ofString(Json.encodeToString(body)))
            .header("Content-Type", "application/json")
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        logger.info(
            """
                response status: ${response.statusCode()}
                response body: ${response.body()}
            """.trimIndent()
        )
    }

    private inline fun <reified T> process(topic: String, body: T) {
        logger.info("process topic: $topic")
        process(URI(buildUrl(topic)), body)
    }

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        process("class-items", codes)
    }

    override fun saveApi(apis: List<ContainerService>) {
        process("container-services", apis)
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        process("datamap-relations", records)
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        process("git-logs", gitLogs)
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        val url = buildUrl("diff-changes") + "&since=${command.since}" + "&until=${command.until}"
        process(URI(url), calls)
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        process("sca-dependencies", dependencies)
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        process("issues", issues)
    }
}
