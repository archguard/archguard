package org.archguard.scanner.core.telemetry

/**
 * Runtime context for LLMOps observability.
 *
 * This is intentionally orthogonal to classic service runtime observability.
 * It extends [RuntimeContext] so it can reuse the same OTLP export pipeline,
 * while adding LLM-specific identity fields.
 */
interface LlmOpsRuntimeContext : RuntimeContext {
    val modelName: String
    val modelProvider: String?
    val modelVersion: String?

    /**
     * Optional logical identifiers for higher-level grouping.
     */
    val promptTemplateId: String?
    val applicationName: String?
}

/**
 * CLI implementation for LLMOps scenario.
 *
 * Uses composition to avoid duplicating base runtime fields.
 */
data class CliLlmOpsRuntimeContext(
    private val base: RuntimeContext,
    override val modelName: String,
    override val modelProvider: String? = null,
    override val modelVersion: String? = null,
    override val promptTemplateId: String? = null,
    override val applicationName: String? = null,
) : LlmOpsRuntimeContext, RuntimeContext by base
