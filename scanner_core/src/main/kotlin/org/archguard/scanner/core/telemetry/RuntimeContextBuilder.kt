package org.archguard.scanner.core.telemetry

import org.archguard.scanner.core.client.ArchGuardClient

/**
 * Builder for [RuntimeContext].
 *
 * Kept separate from [CliRuntimeContext] and sampling logic so we can offer
 * different builders for different scenarios.
 */
class RuntimeContextBuilder {
    private var client: ArchGuardClient? = null
    private var serviceName: String? = null
    private var serviceVersion: String = "unknown"
    private var serviceNamespace: String? = null
    private var serviceInstanceId: String = newRuntimeInstanceId()
    private var deploymentEnvironment: String = "dev"
    private var clusterName: String? = null
    private var hostName: String = currentHostName()
    private var telemetryEndpoint: String = "http://localhost:4317"
    private var telemetryProtocol: TelemetryProtocol = TelemetryProtocol.GRPC
    private var samplingRate: Double = 0.1
    private var enableTracing: Boolean = true
    private var enableMetrics: Boolean = true
    private var enableLogging: Boolean = true
    private var batchSize: Int = 512
    private var batchTimeout: Long = 5000
    private var maxQueueSize: Int = 2048
    private var exportTimeout: Long = 30000
    private var customAttributes: MutableMap<String, String> = mutableMapOf()

    fun client(client: ArchGuardClient) = apply { this.client = client }
    fun serviceName(name: String) = apply { this.serviceName = name }
    fun serviceVersion(version: String) = apply { this.serviceVersion = version }
    fun serviceNamespace(namespace: String?) = apply { this.serviceNamespace = namespace }
    fun serviceInstanceId(id: String) = apply { this.serviceInstanceId = id }
    fun deploymentEnvironment(env: String) = apply { this.deploymentEnvironment = env }
    fun clusterName(name: String?) = apply { this.clusterName = name }
    fun hostName(name: String) = apply { this.hostName = name }

    fun telemetryEndpoint(endpoint: String) = apply { this.telemetryEndpoint = endpoint }
    fun telemetryProtocol(protocol: TelemetryProtocol) = apply { this.telemetryProtocol = protocol }

    fun samplingRate(rate: Double) = apply { this.samplingRate = rate }
    fun enableTracing(enable: Boolean) = apply { this.enableTracing = enable }
    fun enableMetrics(enable: Boolean) = apply { this.enableMetrics = enable }
    fun enableLogging(enable: Boolean) = apply { this.enableLogging = enable }

    fun batchSize(size: Int) = apply { this.batchSize = size }
    fun batchTimeout(timeoutMs: Long) = apply { this.batchTimeout = timeoutMs }
    fun maxQueueSize(size: Int) = apply { this.maxQueueSize = size }
    fun exportTimeout(timeoutMs: Long) = apply { this.exportTimeout = timeoutMs }

    fun attribute(key: String, value: String) = apply { this.customAttributes[key] = value }
    fun attributes(attrs: Map<String, String>) = apply { this.customAttributes.putAll(attrs) }

    fun build(): RuntimeContext {
        requireNotNull(client) { "ArchGuardClient must be set" }
        requireNotNull(serviceName) { "Service name must be set" }

        return CliRuntimeContext(
            client = client!!,
            serviceName = serviceName!!,
            serviceVersion = serviceVersion,
            serviceNamespace = serviceNamespace,
            serviceInstanceId = serviceInstanceId,
            deploymentEnvironment = deploymentEnvironment,
            clusterName = clusterName,
            hostName = hostName,
            telemetryEndpoint = telemetryEndpoint,
            telemetryProtocol = telemetryProtocol,
            samplingRate = samplingRate,
            enableTracing = enableTracing,
            enableMetrics = enableMetrics,
            enableLogging = enableLogging,
            batchSize = batchSize,
            batchTimeout = batchTimeout,
            maxQueueSize = maxQueueSize,
            exportTimeout = exportTimeout,
            customAttributes = customAttributes.toMap(),
        )
    }
}
