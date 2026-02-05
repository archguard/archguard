package org.archguard.trace.receiver

import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceResponse
import io.opentelemetry.proto.collector.logs.v1.LogsServiceGrpc
import mu.KotlinLogging
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

class OtlpGrpcLogsReceiver(
    private val counters: TelemetryCounters,
    private val telemetryStorage: TelemetryStorage
) : LogsServiceGrpc.LogsServiceImplBase() {
    override fun export(
        request: ExportLogsServiceRequest,
        responseObserver: StreamObserver<ExportLogsServiceResponse>
    ) {
        counters.otlpGrpcLogsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC logs export: ${request.resourceLogsCount} resourceLogs" }
        try {
            val records = request.toTelemetryLogRecords()
            kotlinx.coroutines.runBlocking {
                telemetryStorage.storeLogs(records)
            }
        } catch (e: Exception) {
            logger.warn(e) { "Failed to store logs export: ${e.message}" }
        }
        responseObserver.onNext(ExportLogsServiceResponse.newBuilder().build())
        responseObserver.onCompleted()
    }
}

