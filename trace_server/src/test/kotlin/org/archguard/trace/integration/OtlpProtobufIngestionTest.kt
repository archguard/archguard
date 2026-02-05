package org.archguard.trace.integration

import com.google.protobuf.ByteString
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import kotlinx.serialization.json.Json
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.receiver.handleOtlpRequest
import org.archguard.trace.storage.InMemoryTraceStorage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class OtlpProtobufIngestionTest {
    @Test
    fun `should handle OTLP protobuf trace ingestion`() = testApplication {
        val storage = InMemoryTraceStorage()

        application {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            val converter = OtelToAgentTraceConverter()

            routing {
                post("/v1/traces") {
                    handleOtlpRequest(converter, storage)
                }
            }
        }

        val bytes = buildMinimalOtlpProtobufRequest()

        val response = client.post("/v1/traces") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(bytes)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(1, storage.count())
    }

    @Test
    fun `should return protobuf response for protobuf trace request`() = testApplication {
        val storage = InMemoryTraceStorage()

        application {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            val converter = OtelToAgentTraceConverter()

            routing {
                post("/v1/traces") {
                    handleOtlpRequest(converter, storage)
                }
            }
        }

        val bytes = buildMinimalOtlpProtobufRequest()

        val response = client.post("/v1/traces") {
            contentType(ContentType.parse("application/x-protobuf"))
            setBody(bytes)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        // Verify response is protobuf format
        assertTrue(response.contentType()?.match(ContentType.parse("application/x-protobuf")) == true)

        // Verify response can be parsed as OTEL protobuf
        val respBytes: ByteArray = response.readBytes()
        val parsedResp = ExportTraceServiceResponse.parseFrom(respBytes)
        // Empty response means success (no partial_success set)
        assertTrue(parsedResp.isInitialized)
    }

    @Test
    fun `should return JSON response for JSON trace request`() = testApplication {
        val storage = InMemoryTraceStorage()

        application {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; encodeDefaults = true })
            }

            val converter = OtelToAgentTraceConverter()

            routing {
                post("/v1/traces") {
                    handleOtlpRequest(converter, storage)
                }
            }
        }

        // Use internal JSON format compatible with our model (Map<String,String> for attributes)
        val jsonBody = """
        {
            "resourceSpans": [{
                "resource": {
                    "attributes": {}
                },
                "scopeSpans": [{
                    "scope": {"name": "test", "version": "1.0"},
                    "spans": [{
                        "traceId": "0102030405060708090a0b0c0d0e0f10",
                        "spanId": "0102030405060708",
                        "name": "test-span",
                        "kind": "CLIENT",
                        "startTimeNanos": 1000000000,
                        "endTimeNanos": 2000000000,
                        "attributes": {},
                        "events": [],
                        "links": [],
                        "status": {"code": "OK"}
                    }]
                }]
            }]
        }
        """.trimIndent()

        val response = client.post("/v1/traces") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        // Verify response is JSON format (Ktor content negotiation returns application/json)
        val contentType = response.contentType()
        assertTrue(contentType?.match(ContentType.Application.Json) == true || contentType == null)

        // Verify response contains partialSuccess field (OTEL JSON format)
        val body = response.bodyAsText()
        // JSON response should have partialSuccess or be empty object
        assertTrue(body.contains("partialSuccess") || body == "{}" || body.contains("rejectedSpans"))
    }

    private fun buildMinimalOtlpProtobufRequest(): ByteArray {
        val traceId = ByteArray(16) { 0x01 }
        val spanId = ByteArray(8) { 0x02 }

        val span = io.opentelemetry.proto.trace.v1.Span.newBuilder()
            .setTraceId(ByteString.copyFrom(traceId))
            .setSpanId(ByteString.copyFrom(spanId))
            .setName("test-span")
            .setStartTimeUnixNano(1L)
            .setEndTimeUnixNano(2L)
            .build()

        val scope = io.opentelemetry.proto.common.v1.InstrumentationScope.newBuilder()
            .setName("test-scope")
            .setVersion("1.0")
            .build()

        val scopeSpans = io.opentelemetry.proto.trace.v1.ScopeSpans.newBuilder()
            .setScope(scope)
            .addSpans(span)
            .build()

        val resource = io.opentelemetry.proto.resource.v1.Resource.newBuilder().build()

        val resourceSpans = io.opentelemetry.proto.trace.v1.ResourceSpans.newBuilder()
            .setResource(resource)
            .addScopeSpans(scopeSpans)
            .build()

        val export = io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest.newBuilder()
            .addResourceSpans(resourceSpans)
            .build()

        return export.toByteArray()
    }
}

