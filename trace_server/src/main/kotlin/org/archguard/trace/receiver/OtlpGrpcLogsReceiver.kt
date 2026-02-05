package org.archguard.trace.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceResponse
import io.opentelemetry.proto.collector.logs.v1.LogsServiceGrpc
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class OtlpGrpcLogsReceiver(
    private val counters: TelemetryCounters
) : LogsServiceGrpc.LogsServiceImplBase() {
    override fun export(
        request: ExportLogsServiceRequest,
        responseObserver: StreamObserver<ExportLogsServiceResponse>
    ) {
        counters.otlpGrpcLogsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC logs export: ${request.resourceLogsCount} resourceLogs" }
        responseObserver.onNext(ExportLogsServiceResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}

