package org.archguard.scanner.core.telemetry

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.time.Instant

/**
 * OpenTelemetry Trace Models based on OTLP v1.3.0
 * 
 * Represents the core data structures for distributed tracing
 * following OpenTelemetry semantic conventions.
 */

// ============================================================================
// Trace Context & Span
// ============================================================================

@Serializable
data class TraceContext(
    val traceId: String,                    // 128-bit identifier (32 hex chars)
    val spanId: String,                     // 64-bit identifier (16 hex chars)
    val parentSpanId: String? = null,       // Parent span reference
    val traceState: String? = null,         // W3C Trace Context state
    val traceFlags: Byte = 0x01             // Sampled flag
)

@Serializable
data class Span(
    val context: TraceContext,
    val name: String,
    val kind: SpanKind,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long? = null,
    val attributes: Map<String, AttributeValue> = emptyMap(),
    val events: List<SpanEvent> = emptyList(),
    val links: List<SpanLink> = emptyList(),
    val status: SpanStatus,
    val resource: Resource
) {
    val durationNano: Long?
        get() = endTimeUnixNano?.let { it - startTimeUnixNano }
    
    val durationMs: Long?
        get() = durationNano?.let { it / 1_000_000 }
}

enum class SpanKind {
    INTERNAL,           // Internal operation
    SERVER,             // Server handling a request
    CLIENT,             // Client sending a request
    PRODUCER,           // Message producer
    CONSUMER            // Message consumer
}

@Serializable
data class SpanStatus(
    val code: StatusCode,
    val message: String? = null
)

enum class StatusCode {
    UNSET,              // Default status
    OK,                 // Success
    ERROR               // Error occurred
}

@Serializable
data class SpanEvent(
    val name: String,
    val timeUnixNano: Long,
    val attributes: Map<String, AttributeValue> = emptyMap()
)

@Serializable
data class SpanLink(
    val traceId: String,
    val spanId: String,
    val traceState: String? = null,
    val attributes: Map<String, AttributeValue> = emptyMap()
)

// ============================================================================
// Metrics
// ============================================================================

@Serializable
sealed class Metric {
    abstract val name: String
    abstract val description: String
    abstract val unit: String
    abstract val attributes: Map<String, AttributeValue>
    abstract val timeUnixNano: Long
}

@Serializable
data class Counter(
    override val name: String,
    override val description: String = "",
    override val unit: String = "1",
    override val attributes: Map<String, AttributeValue> = emptyMap(),
    override val timeUnixNano: Long,
    val value: Long,
    val isMonotonic: Boolean = true
) : Metric()

@Serializable
data class Gauge(
    override val name: String,
    override val description: String = "",
    override val unit: String,
    override val attributes: Map<String, AttributeValue> = emptyMap(),
    override val timeUnixNano: Long,
    val value: Double
) : Metric()

@Serializable
data class Histogram(
    override val name: String,
    override val description: String = "",
    override val unit: String = "ms",
    override val attributes: Map<String, AttributeValue> = emptyMap(),
    override val timeUnixNano: Long,
    val dataPoints: List<HistogramDataPoint>
) : Metric()

@Serializable
data class HistogramDataPoint(
    val sum: Double,
    val count: Long,
    val min: Double? = null,
    val max: Double? = null,
    val bucketCounts: List<Long>,           // Counts per bucket
    val explicitBounds: List<Double>        // Bucket boundaries
)

@Serializable
data class Summary(
    override val name: String,
    override val description: String = "",
    override val unit: String = "ms",
    override val attributes: Map<String, AttributeValue> = emptyMap(),
    override val timeUnixNano: Long,
    val sum: Double,
    val count: Long,
    val quantiles: List<Quantile>
) : Metric()

@Serializable
data class Quantile(
    val quantile: Double,                   // 0.0 to 1.0 (e.g., 0.95 for P95)
    val value: Double
)

// ============================================================================
// Logs
// ============================================================================

@Serializable
data class LogRecord(
    val timeUnixNano: Long,
    val observedTimeUnixNano: Long,
    val severityNumber: Int,                // 1-24 (DEBUG=5, INFO=9, WARN=13, ERROR=17)
    val severityText: String,
    val body: AttributeValue,
    val attributes: Map<String, AttributeValue> = emptyMap(),
    val traceId: String? = null,
    val spanId: String? = null,
    val traceFlags: Byte? = null,
    val resource: Resource
)

object SeverityNumber {
    const val TRACE = 1
    const val TRACE2 = 2
    const val TRACE3 = 3
    const val TRACE4 = 4
    const val DEBUG = 5
    const val DEBUG2 = 6
    const val DEBUG3 = 7
    const val DEBUG4 = 8
    const val INFO = 9
    const val INFO2 = 10
    const val INFO3 = 11
    const val INFO4 = 12
    const val WARN = 13
    const val WARN2 = 14
    const val WARN3 = 15
    const val WARN4 = 16
    const val ERROR = 17
    const val ERROR2 = 18
    const val ERROR3 = 19
    const val ERROR4 = 20
    const val FATAL = 21
    const val FATAL2 = 22
    const val FATAL3 = 23
    const val FATAL4 = 24
}

// ============================================================================
// Events
// ============================================================================

@Serializable
data class Event(
    val name: String,
    val domain: EventDomain,
    val timeUnixNano: Long,
    val attributes: Map<String, AttributeValue> = emptyMap(),
    val traceContext: TraceContext? = null
)

enum class EventDomain {
    SYSTEM,             // System-level events (e.g., server start)
    APPLICATION,        // Application-level events (e.g., user action)
    SECURITY,           // Security events (e.g., auth failure)
    BUSINESS            // Business events (e.g., order placed)
}

// ============================================================================
// Resource & Attributes
// ============================================================================

@Serializable
data class Resource(
    val attributes: Map<String, AttributeValue>
) {
    companion object {
        fun create(
            serviceName: String,
            serviceVersion: String? = null,
            serviceNamespace: String? = null,
            serviceInstanceId: String? = null,
            deploymentEnvironment: String? = null
        ): Resource {
            val attrs = mutableMapOf<String, AttributeValue>()
            attrs["service.name"] = AttributeValue.StringValue(serviceName)
            serviceVersion?.let { attrs["service.version"] = AttributeValue.StringValue(it) }
            serviceNamespace?.let { attrs["service.namespace"] = AttributeValue.StringValue(it) }
            serviceInstanceId?.let { attrs["service.instance.id"] = AttributeValue.StringValue(it) }
            deploymentEnvironment?.let { attrs["deployment.environment"] = AttributeValue.StringValue(it) }
            return Resource(attrs)
        }
    }
}

@Serializable
sealed class AttributeValue {
    @Serializable
    data class StringValue(val value: String) : AttributeValue()
    
    @Serializable
    data class IntValue(val value: Long) : AttributeValue()
    
    @Serializable
    data class DoubleValue(val value: Double) : AttributeValue()
    
    @Serializable
    data class BoolValue(val value: Boolean) : AttributeValue()
    
    @Serializable
    data class ArrayValue(val values: List<AttributeValue>) : AttributeValue()
    
    @Serializable
    data class MapValue(val values: Map<String, AttributeValue>) : AttributeValue()
}

// ============================================================================
// LLM-specific Telemetry Models
// ============================================================================

@Serializable
data class LlmSpan(
    val baseSpan: Span,
    
    // LLM Provider & Model
    val llmProvider: String,                // openai, anthropic, cohere
    val llmModel: String,                   // gpt-4, claude-3-opus
    val llmEndpoint: String? = null,
    
    // Request
    val llmPrompt: String,
    val llmSystemMessage: String? = null,
    val llmMessages: List<LlmMessage> = emptyList(),
    
    // Response
    val llmCompletion: String,
    val llmFinishReason: String,            // stop, length, content_filter
    
    // Token Usage
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    
    // Cost
    val estimatedCostUsd: Double,
    val costPerToken: Double,
    
    // Performance
    val timeToFirstTokenMs: Long? = null,
    val tokensPerSecond: Double? = null,
    
    // Request Parameters
    val temperature: Double? = null,
    val topP: Double? = null,
    val maxTokens: Int? = null,
    val presencePenalty: Double? = null,
    val frequencyPenalty: Double? = null,
    
    // Metadata
    val userId: String? = null,
    val sessionId: String? = null
)

@Serializable
data class LlmMessage(
    val role: String,                       // system, user, assistant
    val content: String,
    val name: String? = null
)

@Serializable
data class VectorDbSpan(
    val baseSpan: Span,
    
    val dbSystem: String,                   // pinecone, weaviate, chroma
    val dbOperation: String,                // query, insert, update, delete
    val collectionName: String,
    val namespace: String? = null,
    
    // Query Details
    val vectorDimension: Int? = null,
    val topK: Int? = null,
    val similarityMetric: String? = null,   // cosine, euclidean, dotProduct
    val filter: Map<String, JsonElement>? = null,
    
    // Results
    val resultCount: Int,
    val results: List<VectorSearchResult> = emptyList()
)

@Serializable
data class VectorSearchResult(
    val id: String,
    val score: Double,
    val metadata: Map<String, AttributeValue> = emptyMap()
)

@Serializable
data class AgentWorkflow(
    val workflowId: String,
    val workflowName: String,
    val agentType: String,                  // react, plan-execute, reflexion
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long? = null,
    
    val tasks: List<AgentTask>,
    val totalSteps: Int,
    val successfulSteps: Int,
    val failedSteps: Int,
    
    val finalOutput: String? = null,
    val status: String                      // running, completed, failed
)

@Serializable
data class AgentTask(
    val taskId: String,
    val taskType: String,                   // tool_call, reasoning, observation
    val stepNumber: Int,
    
    val input: String,
    val output: String,
    
    val toolUsed: String? = null,           // calculator, web_search, database
    val toolInput: Map<String, JsonElement>? = null,
    val toolOutput: JsonElement? = null,
    
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
    val status: String                      // success, failed
)

// ============================================================================
// Semantic Conventions
// ============================================================================

object SemanticConventions {
    
    object Http {
        const val METHOD = "http.method"
        const val URL = "http.url"
        const val TARGET = "http.target"
        const val HOST = "http.host"
        const val SCHEME = "http.scheme"
        const val STATUS_CODE = "http.status_code"
        const val USER_AGENT = "http.user_agent"
        const val REQUEST_CONTENT_LENGTH = "http.request.content_length"
        const val RESPONSE_CONTENT_LENGTH = "http.response.content_length"
        const val ROUTE = "http.route"
        const val CLIENT_IP = "http.client_ip"
    }
    
    object Db {
        const val SYSTEM = "db.system"
        const val CONNECTION_STRING = "db.connection_string"
        const val USER = "db.user"
        const val NAME = "db.name"
        const val STATEMENT = "db.statement"
        const val OPERATION = "db.operation"
        const val SQL_TABLE = "db.sql.table"
    }
    
    object Rpc {
        const val SYSTEM = "rpc.system"
        const val SERVICE = "rpc.service"
        const val METHOD = "rpc.method"
        const val GRPC_STATUS_CODE = "rpc.grpc.status_code"
    }
    
    object Messaging {
        const val SYSTEM = "messaging.system"
        const val DESTINATION = "messaging.destination"
        const val DESTINATION_KIND = "messaging.destination_kind"
        const val PROTOCOL = "messaging.protocol"
        const val OPERATION = "messaging.operation"
        const val MESSAGE_ID = "messaging.message_id"
    }
    
    object Llm {
        const val PROVIDER = "llm.provider"
        const val MODEL = "llm.model"
        const val PROMPT = "llm.prompt"
        const val COMPLETION = "llm.completion"
        const val TEMPERATURE = "llm.temperature"
        const val MAX_TOKENS = "llm.max_tokens"
        const val PROMPT_TOKENS = "llm.usage.prompt_tokens"
        const val COMPLETION_TOKENS = "llm.usage.completion_tokens"
        const val TOTAL_TOKENS = "llm.usage.total_tokens"
        const val COST = "llm.cost.total"
        const val FINISH_REASON = "llm.finish_reason"
    }
    
    object VectorDb {
        const val SYSTEM = "vectordb.system"
        const val OPERATION = "vectordb.operation"
        const val COLLECTION = "vectordb.collection"
        const val VECTOR_DIMENSION = "vectordb.vector.dimension"
        const val TOP_K = "vectordb.query.top_k"
        const val SIMILARITY_METRIC = "vectordb.similarity.metric"
    }
}

// ============================================================================
// Telemetry Batch
// ============================================================================

@Serializable
data class TelemetryBatch(
    val traces: List<Span> = emptyList(),
    val metrics: List<Metric> = emptyList(),
    val logs: List<LogRecord> = emptyList(),
    val events: List<Event> = emptyList()
)
