package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.client.dto.CodeDatabaseRelation
import org.archguard.scanner.core.client.dto.ContainerService

// 通过http api回写分析数据
class ArchGuardHttpClient(
    private val language: String,
    private val serverUrl: String,
    private val systemId: String,
    private val path: String
) : ArchGuardClient {
    private fun buildUrl(topic: String) = serverUrl +
        "/api/scanner/$systemId/reporting/$topic" +
        "?language=$language" +
        "&path=$path"

    override fun saveDataStructure(codes: List<CodeDataStruct>): Unit = runBlocking {
        HttpClient().use {
            it.post(buildUrl("class-items")) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(codes))
            }
        }
    }

    override fun saveApi(apis: List<ContainerService>): Unit = runBlocking {
        HttpClient().use {
            it.post(buildUrl("container-services")) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(apis))
            }
        }
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>): Unit = runBlocking {
        HttpClient().use {
            it.post(buildUrl("datamap-relations")) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(records))
            }
        }
    }
}
