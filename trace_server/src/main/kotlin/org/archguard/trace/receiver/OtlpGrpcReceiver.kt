package org.archguard.trace.receiver

import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
    private val counters: TelemetryCounters? = null,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : TraceServiceGrpc.TraceServiceImplBase() {
    private val receiver = OtelTraceReceiver(converter, storage)

    override fun export(
        request: ExportTraceServiceRequest,
        responseObserver: StreamObserver<ExportTraceServiceResponse>
    ) {
        counters?.otlpGrpcTraceRequests?.incrementAndGet()
        logger.info { "OTLP/gRPC export: ${request.resourceSpansCount} resourceSpans" }

        // Convert protobuf request into our simplified model and reuse existing processing pipeline.
        val modelReq = try {
            request.toModelRequest()
        } catch (e: Exception) {
            logger.warn(e) { "Failed to parse OTLP/gRPC export request: ${e.message}" }
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid OTLP export request")
                    .withCause(e)
                    .asRuntimeException()
            )
            return
        }

        // Run the suspend pipeline off the gRPC handler thread to avoid thread starvation under load.
        scope.launch {
            try {
                receiver.receiveOtlpTraces(modelReq)
                responseObserver.onNext(ExportTraceServiceResponse.newBuilder().build())
                responseObserver.onCompleted()
            } catch (e: Exception) {
                logger.error(e) { "Failed to handle OTLP/gRPC export: ${e.message}" }
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("Failed to ingest traces")
                        .withCause(e)
                        .asRuntimeException()
                )
            }
        }
    }
}

