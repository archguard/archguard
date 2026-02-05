package org.archguard.trace.integration

import com.google.protobuf.ByteString
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.receiver.handleOtlpRequest
import org.archguard.trace.storage.InMemoryTraceStorage
import org.junit.jupiter.api.Assertions.assertEquals
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

