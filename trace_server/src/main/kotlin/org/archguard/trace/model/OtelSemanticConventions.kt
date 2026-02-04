package org.archguard.trace.model

/**
 * OpenTelemetry Semantic Conventions for Agent Trace
 * 
 * This defines custom semantic conventions in the `code.*` namespace
 * for code attribution and AI code generation tracking.
 * 
 * Also includes standard GenAI semantic conventions from OpenTelemetry.
 */
object OtelSemanticConventions {
    
    // ============= GenAI Standard Semantic Conventions =============
    // These follow OpenTelemetry GenAI semantic conventions (experimental)
    
    object GenAI {
        /** AI system/provider name (anthropic, openai, google, etc.) */
        const val SYSTEM = "gen_ai.system"
        
        /** AI model identifier */
        const val REQUEST_MODEL = "gen_ai.request.model"
        
        /** Temperature parameter */
        const val REQUEST_TEMPERATURE = "gen_ai.request.temperature"
        
        /** Maximum tokens parameter */
        const val REQUEST_MAX_TOKENS = "gen_ai.request.max_tokens"
        
        /** Top-p parameter */
        const val REQUEST_TOP_P = "gen_ai.request.top_p"
        
        /** Response finish reason (stop, length, tool_use, etc.) */
        const val RESPONSE_FINISH_REASON = "gen_ai.response.finish_reason"
        
        /** Number of prompt tokens */
        const val USAGE_PROMPT_TOKENS = "gen_ai.usage.prompt_tokens"
        
        /** Number of completion tokens */
        const val USAGE_COMPLETION_TOKENS = "gen_ai.usage.completion_tokens"
        
        /** Total number of tokens */
        const val USAGE_TOTAL_TOKENS = "gen_ai.usage.total_tokens"
    }
    
    // ============= Code Attribution Semantic Conventions =============
    // Custom namespace for code attribution
    
    object Code {
        /** Contributor type: ai, human, mixed, unknown */
        const val CONTRIBUTOR_TYPE = "code.contributor.type"
        
        /** AI model identifier (full path: provider/model) */
        const val CONTRIBUTOR_MODEL = "code.contributor.model"
        
        /** AI coding tool name (cursor, windsurf, copilot, etc.) */
        const val CONTRIBUTOR_TOOL = "code.contributor.tool"
        
        /** AI coding tool version */
        const val CONTRIBUTOR_TOOL_VERSION = "code.contributor.tool_version"
        
        /** File path being generated/modified */
        const val GENERATION_FILE = "code.generation.file"
        
        /** Start line of generated code range */
        const val RANGE_START = "code.generation.range.start"
        
        /** End line of generated code range */
        const val RANGE_END = "code.generation.range.end"
        
        /** Content hash of generated code */
        const val RANGE_HASH = "code.generation.range.hash"
        
        /** Programming language */
        const val LANGUAGE = "code.generation.language"
        
        /** Number of lines generated */
        const val RANGE_LINES = "code.range.lines"
    }
    
    // ============= VCS Semantic Conventions =============
    
    object Vcs {
        /** VCS type: git, jj, hg, svn */
        const val TYPE = "vcs.type"
        
        /** Commit hash or revision identifier */
        const val REVISION = "vcs.revision"
        
        /** Repository URL */
        const val REPOSITORY = "vcs.repository"
        
        /** Branch name */
        const val BRANCH = "vcs.branch"
    }
    
    // ============= Conversation Semantic Conventions =============
    
    object Conversation {
        /** Conversation URL */
        const val URL = "conversation.url"
        
        /** Conversation ID */
        const val ID = "conversation.id"
        
        /** Turn number in conversation */
        const val TURN = "conversation.turn"
    }
    
    // ============= Quality Metrics Semantic Conventions =============
    
    object Quality {
        /** Code complexity metric */
        const val COMPLEXITY = "code.quality.complexity"
        
        /** Number of violations */
        const val VIOLATIONS = "code.quality.violations"
        
        /** Code coverage */
        const val COVERAGE = "code.quality.coverage"
        
        /** Quality score */
        const val SCORE = "code.quality.score"
    }
    
    // ============= Span Names =============
    
    object SpanNames {
        /** Root span for a trace record */
        const val TRACE_RECORD = "agent_trace_record"
        
        /** Span for code generation conversation */
        const val GENERATE_CODE = "generate_code"
        
        /** Span for code range generation */
        const val CODE_RANGE_GENERATED = "code.range.generated"
    }
    
    // ============= Link Types =============
    
    object LinkTypes {
        /** Link type */
        const val TYPE = "link.type"
        
        /** Link URL */
        const val URL = "link.url"
    }
    
    // ============= Resource Attributes =============
    
    object Resource {
        /** Service name */
        const val SERVICE_NAME = "service.name"
        
        /** Service version */
        const val SERVICE_VERSION = "service.version"
        
        /** Host name */
        const val HOST_NAME = "host.name"
        
        /** Service namespace */
        const val SERVICE_NAMESPACE = "service.namespace"
    }
}

/**
 * Helper class to build OTEL attributes for Agent Trace
 */
class OtelAttributeBuilder {
    private val attributes = mutableMapOf<String, Any>()
    
    fun addContributor(contributor: Contributor): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TYPE] = contributor.type
        
        contributor.modelId?.let { modelId ->
            attributes[OtelSemanticConventions.Code.CONTRIBUTOR_MODEL] = modelId
            
            // Parse provider from model_id (e.g., "anthropic/claude-opus" -> "anthropic")
            val provider = modelId.split("/").firstOrNull()
            if (provider != null) {
                attributes[OtelSemanticConventions.GenAI.SYSTEM] = provider
                attributes[OtelSemanticConventions.GenAI.REQUEST_MODEL] = modelId
            }
        }
        
        return this
    }
    
    fun addVcsInfo(vcs: VcsInfo): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Vcs.TYPE] = vcs.type
        attributes[OtelSemanticConventions.Vcs.REVISION] = vcs.revision
        vcs.repository?.let { attributes[OtelSemanticConventions.Vcs.REPOSITORY] = it }
        vcs.branch?.let { attributes[OtelSemanticConventions.Vcs.BRANCH] = it }
        return this
    }
    
    fun addToolInfo(tool: ToolInfo): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TOOL] = tool.name
        tool.version?.let { attributes[OtelSemanticConventions.Code.CONTRIBUTOR_TOOL_VERSION] = it }
        return this
    }
    
    fun addFile(path: String): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Code.GENERATION_FILE] = path
        return this
    }
    
    fun addConversation(url: String): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Conversation.URL] = url
        return this
    }
    
    fun addRange(range: Range): OtelAttributeBuilder {
        attributes[OtelSemanticConventions.Code.RANGE_START] = range.startLine
        attributes[OtelSemanticConventions.Code.RANGE_END] = range.endLine
        range.contentHash?.let { attributes[OtelSemanticConventions.Code.RANGE_HASH] = it }
        attributes[OtelSemanticConventions.Code.RANGE_LINES] = range.endLine - range.startLine + 1
        return this
    }
    
    fun addCustomAttribute(key: String, value: Any): OtelAttributeBuilder {
        attributes[key] = value
        return this
    }
    
    fun build(): Map<String, Any> = attributes.toMap()
}
