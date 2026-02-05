package org.archguard.trace.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import mu.KotlinLogging
import org.archguard.trace.converter.OtelToAgentTraceConverter
import org.archguard.trace.storage.TraceStorage

private val logger = KotlinLogging.logger {}

/**
 * OTLP gRPC receiver implementation.
 *
 * Many OTEL SDKs default to OTLP/gRPC on port 4317.
 * Adding this receiver makes `trace_server` compatible with those defaults.
 */
class OtlpGrpcReceiver(
    private val converter: OtelToAgentTraceConverter,
    private val storage: TraceStorage,
    private val counters: TelemetryCounters? = null
) : TraceServiceGrpc.TraceServiceImplBase() {
    private val receiver = OtelTraceReceiver(converter, storage)

    override fun export(
        request: ExportTraceServiceRequest,
        responseObserver: StreamObserver<ExportTraceServiceResponse>
    ) {
        try {
            counters?.otlpGrpcTraceRequests?.incrementAndGet()
            logger.info { "OTLP/gRPC export: ${request.resourceSpansCount} resourceSpans" }

            // Convert protobuf request into our simplified model and reuse existing processing pipeline.
            val modelReq = request.toModelRequest()

            // Run suspend pipeline in a blocking way (gRPC thread).
            // This keeps the implementation small; we can optimize with coroutines later if needed.
            kotlinx.coroutines.runBlocking {
                receiver.receiveOtlpTraces(modelReq)
            }

            responseObserver.onNext(ExportTraceServiceResponse.newBuilder().build())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error(e) { "Failed to handle OTLP/gRPC export: ${e.message}" }
            responseObserver.onError(e)
        }
    }
}

