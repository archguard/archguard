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
import org.archguard.scanner.ctl.command.ScannerCommand
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpResponse
import java.time.Duration

val String.encodeUri: String get() = URLEncoder.encode(this, "UTF-8")

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

    fun buildUrl(topic: String): String {
        val url = "$serverUrl/api/scanner/$systemId/reporting/$topic?language=$language&path=${path.encodeUri}"
        if (command.repoId != null) {
            return url + "&repoId=${command.repoId}"
        }

        return url
    }

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

    override fun saveEstimates(estimates: List<LanguageEstimate>) {
        process("estimates", estimates)
    }

    override fun saveOpenApi(collections: List<ApiCollection>) {
        process("openapi", collections)
    }
}
