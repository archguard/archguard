package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService
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
    private val path: String
) : ArchGuardClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val client: HttpClient by lazy {
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofMinutes(2))
            .build()
    }

    private fun buildUrl(topic: String) =
        "$serverUrl/api/scanner/$systemId/reporting/$topic?language=$language&path=$path"

    private inline fun <reified T> process(topic: String, body: T) {
        val request = HttpRequest.newBuilder(URI(buildUrl(topic)))
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

    // create another job to execute this coroutine
    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        process("class-items", codes)
    }

    override fun saveApi(apis: List<ContainerService>) {
        process("container-services", apis)
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        process("datamap-relations", records)
    }
}
