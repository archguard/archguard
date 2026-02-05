package org.archguard.trace.receiver

import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse
import io.opentelemetry.proto.collector.metrics.v1.MetricsServiceGrpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

class OtlpGrpcMetricsReceiver(
    private val counters: TelemetryCounters,
    private val telemetryStorage: TelemetryStorage,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : MetricsServiceGrpc.MetricsServiceImplBase() {
    override fun export(
        request: ExportMetricsServiceRequest,
        responseObserver: StreamObserver<ExportMetricsServiceResponse>
    ) {
        counters.otlpGrpcMetricsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC metrics export: ${request.resourceMetricsCount} resourceMetrics" }
        val points = try {
            request.toTelemetryMetricPoints()
        } catch (e: Exception) {
            logger.warn(e) { "Failed to parse metrics export request: ${e.message}" }
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid metrics export request")
                    .withCause(e)
                    .asRuntimeException()
            )
            return
        }

        // Store off the gRPC handler thread to avoid blocking the gRPC thread pool.
        scope.launch {
            try {
                telemetryStorage.storeMetrics(points)
                responseObserver.onNext(ExportMetricsServiceResponse.newBuilder().build())
                responseObserver.onCompleted()
            } catch (e: Exception) {
                logger.warn(e) { "Failed to store metrics export: ${e.message}" }
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("Failed to ingest metrics")
                        .withCause(e)
                        .asRuntimeException()
                )
            }
        }
    }
}

