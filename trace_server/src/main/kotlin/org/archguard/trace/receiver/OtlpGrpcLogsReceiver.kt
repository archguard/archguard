package org.archguard.trace.receiver

import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceRequest
import io.opentelemetry.proto.collector.logs.v1.ExportLogsServiceResponse
import io.opentelemetry.proto.collector.logs.v1.LogsServiceGrpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.archguard.trace.storage.TelemetryStorage

private val logger = KotlinLogging.logger {}

class OtlpGrpcLogsReceiver(
    private val counters: TelemetryCounters,
    private val telemetryStorage: TelemetryStorage,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : LogsServiceGrpc.LogsServiceImplBase() {
    override fun export(
        request: ExportLogsServiceRequest,
        responseObserver: StreamObserver<ExportLogsServiceResponse>
    ) {
        counters.otlpGrpcLogsRequests.incrementAndGet()
        logger.info { "OTLP/gRPC logs export: ${request.resourceLogsCount} resourceLogs" }
        val records = try {
            request.toTelemetryLogRecords()
        } catch (e: Exception) {
            logger.warn(e) { "Failed to parse logs export request: ${e.message}" }
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Invalid logs export request")
                    .withCause(e)
                    .asRuntimeException()
            )
            return
        }

        // Store off the gRPC handler thread to avoid blocking the gRPC thread pool.
        scope.launch {
            try {
                telemetryStorage.storeLogs(records)
                responseObserver.onNext(ExportLogsServiceResponse.newBuilder().build())
                responseObserver.onCompleted()
            } catch (e: Exception) {
                logger.warn(e) { "Failed to store logs export: ${e.message}" }
                responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("Failed to ingest logs")
                        .withCause(e)
                        .asRuntimeException()
                )
            }
        }
    }
}

