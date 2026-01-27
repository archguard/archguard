package org.archguard.scanner.core.telemetry

import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import java.net.InetAddress
import java.util.UUID

/**
 * Runtime monitoring context for APM and observability
 * 
 * This context enables distributed tracing, metrics collection,
 * and log aggregation for ArchGuard services at runtime.
 */
interface RuntimeContext : Context {
    override val type: AnalyserType get() = AnalyserType.RUNTIME
    
    // Service Identity
    val serviceName: String
    val serviceVersion: String
    val serviceNamespace: String?
    val serviceInstanceId: String
    
    // Environment
    val deploymentEnvironment: String           // dev, staging, production
    val clusterName: String?
    val hostName: String
    
    // Telemetry Configuration
    val telemetryEndpoint: String               // OTLP collector endpoint
    val telemetryProtocol: TelemetryProtocol
    val samplingRate: Double                    // 0.0 to 1.0
    val enableTracing: Boolean
    val enableMetrics: Boolean
    val enableLogging: Boolean
    
    // Export Configuration
    val batchSize: Int
    val batchTimeout: Long                      // milliseconds
    val maxQueueSize: Int
    val exportTimeout: Long                     // milliseconds
    
    // Resource Attributes
    val customAttributes: Map<String, String>
}

enum class TelemetryProtocol {
    GRPC,                   // gRPC protocol (default)
    HTTP_PROTOBUF,          // HTTP with Protobuf encoding
    HTTP_JSON               // HTTP with JSON encoding
}

internal fun newRuntimeInstanceId(): String = UUID.randomUUID().toString()

internal fun currentHostName(): String {
    return try {
        InetAddress.getLocalHost().hostName
    } catch (_: Exception) {
        "unknown"
    }
}

/**
 * CLI implementation of RuntimeContext
 */
data class CliRuntimeContext(
    override val client: ArchGuardClient,
    override val serviceName: String,
    override val serviceVersion: String = "unknown",
    override val serviceNamespace: String? = null,
    override val serviceInstanceId: String = newRuntimeInstanceId(),
    override val deploymentEnvironment: String = "dev",
    override val clusterName: String? = null,
    override val hostName: String = currentHostName(),
    override val telemetryEndpoint: String = "http://localhost:4317",
    override val telemetryProtocol: TelemetryProtocol = TelemetryProtocol.GRPC,
    override val samplingRate: Double = 0.1,
    override val enableTracing: Boolean = true,
    override val enableMetrics: Boolean = true,
    override val enableLogging: Boolean = true,
    override val batchSize: Int = 512,
    override val batchTimeout: Long = 5000,
    override val maxQueueSize: Int = 2048,
    override val exportTimeout: Long = 30000,
    override val customAttributes: Map<String, String> = emptyMap()
) : RuntimeContext {

    /**
     * Create Resource from context
     */
    fun toResource(): Resource {
        val attributes = mutableMapOf<String, AttributeValue>()
        
        // Service attributes
        attributes["service.name"] = AttributeValue.StringValue(serviceName)
        attributes["service.version"] = AttributeValue.StringValue(serviceVersion)
        attributes["service.instance.id"] = AttributeValue.StringValue(serviceInstanceId)
        
        serviceNamespace?.let {
            attributes["service.namespace"] = AttributeValue.StringValue(it)
        }
        
        // Deployment attributes
        attributes["deployment.environment"] = AttributeValue.StringValue(deploymentEnvironment)
        attributes["host.name"] = AttributeValue.StringValue(hostName)
        
        clusterName?.let {
            attributes["k8s.cluster.name"] = AttributeValue.StringValue(it)
        }
        
        // Runtime attributes
        attributes["telemetry.sdk.name"] = AttributeValue.StringValue("archguard-telemetry")
        attributes["telemetry.sdk.version"] = AttributeValue.StringValue("1.0.0")
        attributes["telemetry.sdk.language"] = AttributeValue.StringValue("kotlin")
        
        // Custom attributes
        customAttributes.forEach { (key, value) ->
            attributes[key] = AttributeValue.StringValue(value)
        }
        
        return Resource(attributes)
    }
}

