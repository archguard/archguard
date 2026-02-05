package org.archguard.trace.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse
import io.opentelemetry.proto.collector.metrics.v1.MetricsServiceGrpc
import mu.KotlinLogging
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

class OtlpGrpcMetricsReceiver(
    private val counters: TelemetryCounters,
    private val telemetryStorage: TelemetryStorage
) : MetricsServiceGrpc.MetricsServiceImplBase() {
    override fun export(
        request: ExportMetricsServiceRequest,
        responseObserver: StreamObserver<ExportMetricsServiceResponse>
    ) {
        counters.otlpGrpcMetricsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC metrics export: ${request.resourceMetricsCount} resourceMetrics" }
        try {
            val points = request.toTelemetryMetricPoints()
            kotlinx.coroutines.runBlocking {
                telemetryStorage.storeMetrics(points)
            }
        } catch (e: Exception) {
            logger.warn(e) { "Failed to store metrics export: ${e.message}" }
        }
        responseObserver.onNext(ExportMetricsServiceResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}

