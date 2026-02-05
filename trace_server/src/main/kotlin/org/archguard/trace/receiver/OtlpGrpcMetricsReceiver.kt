package org.archguard.trace.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceRequest
import io.opentelemetry.proto.collector.metrics.v1.ExportMetricsServiceResponse
import io.opentelemetry.proto.collector.metrics.v1.MetricsServiceGrpc
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class OtlpGrpcMetricsReceiver(
    private val counters: TelemetryCounters
) : MetricsServiceGrpc.MetricsServiceImplBase() {
    override fun export(
        request: ExportMetricsServiceRequest,
        responseObserver: StreamObserver<ExportMetricsServiceResponse>
    ) {
        counters.otlpGrpcMetricsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC metrics export: ${request.resourceMetricsCount} resourceMetrics" }
        responseObserver.onNext(ExportMetricsServiceResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}

