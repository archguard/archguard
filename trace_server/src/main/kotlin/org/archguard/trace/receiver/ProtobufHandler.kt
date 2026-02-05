package org.archguard.trace.receiver

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import mu.KotlinLogging
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.storage.TraceStorage

private val logger = KotlinLogging.logger {}

/**
 * OTEL Protobuf Request Handler
 * 
 * Handles both JSON and Protobuf formats for OTLP trace ingestion.
 * This implementation provides basic protobuf parsing for the OTEL trace format.
 */
suspend fun PipelineContext<Unit, ApplicationCall>.handleOtlpRequest(
    converter: OtelToAgentTraceConverter,
    storage: TraceStorage
) {
    val contentType = call.request.contentType()
    
    try {
        when {
            contentType.match(ContentType.Application.Json) -> {
                handleJsonRequest(converter, storage)
            }
            contentType.match(ContentType.Application.ProtoBuf) ||
            contentType.toString().contains("application/x-protobuf") -> {
                handleProtobufRequest(converter, storage)
            }
            else -> {
                logger.warn { "Unsupported content type: $contentType" }
                call.respond(
                    HttpStatusCode.UnsupportedMediaType,
                    mapOf("error" to "Unsupported content type: $contentType. Use application/json or application/x-protobuf")
                )
            }
        }
    } catch (e: Exception) {
        logger.error(e) { "Failed to process OTLP request: ${e.message}" }
        call.respond(
            HttpStatusCode.InternalServerError,
            mapOf("error" to (e.message ?: "Unknown error"))
        )
    }
}

/**
 * Handle JSON format OTLP request
 */
private suspend fun PipelineContext<Unit, ApplicationCall>.handleJsonRequest(
    converter: OtelToAgentTraceConverter,
    storage: TraceStorage
) {
    val request = call.receive<OtlpExportRequest>()
    val receiver = OtelTraceReceiver(converter, storage)
    val response = receiver.receiveOtlpTraces(request)
    call.respond(HttpStatusCode.OK, response)
}

/**
 * Handle Protobuf format OTLP request
 * 
 * This is a simplified implementation that extracts trace data from the protobuf format.
 * For a production system, you would use the official OTLP protobuf definitions.
 */
private suspend fun PipelineContext<Unit, ApplicationCall>.handleProtobufRequest(
    converter: OtelToAgentTraceConverter,
    storage: TraceStorage
) {
    val bytes = call.receive<ByteArray>()
    logger.info { "Received protobuf OTLP request: ${bytes.size} bytes" }

    val proto = ExportTraceServiceRequest.parseFrom(bytes)
    val request = proto.toModelRequest()

    val receiver = OtelTraceReceiver(converter, storage)
    val response = receiver.receiveOtlpTraces(request)
    call.respond(HttpStatusCode.OK, response)
}
