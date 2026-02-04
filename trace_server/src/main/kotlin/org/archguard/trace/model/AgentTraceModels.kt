package org.archguard.trace.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Agent Trace Record
 * 
 * The fundamental unit containing AI code generation attribution data.
 * Follows Agent Trace specification v0.1.0
 * 
 * @property version Specification version (e.g., "0.1.0")
 * @property id Unique identifier for the trace record (UUID)
 * @property timestamp RFC3339 timestamp
 * @property vcs Version control system information
 * @property tool AI coding tool information
 * @property files List of files with conversation attributions
 * @property metadata Optional metadata
 */
@Serializable
data class TraceRecord(
    val version: String,
    val id: String,
    val timestamp: String,
    val vcs: VcsInfo,
    val tool: ToolInfo,
    val files: List<TraceFile>,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Version Control System information
 * 
 * @property type VCS type: git, jj (Jujutsu), hg (Mercurial), svn
 * @property revision Commit hash or revision identifier
 * @property repository Optional repository URL
 * @property branch Optional branch name
 */
@Serializable
data class VcsInfo(
    val type: String,
    val revision: String,
    val repository: String? = null,
    val branch: String? = null
)

/**
 * AI Coding Tool information
 * 
 * @property name Tool name (e.g., cursor, windsurf, copilot)
 * @property version Tool version
 * @property metadata Optional tool-specific metadata
 */
@Serializable
data class ToolInfo(
    val name: String,
    val version: String? = null,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * File with conversation attributions
 * 
 * @property path File path relative to repository root
 * @property conversations List of conversations that generated code in this file
 */
@Serializable
data class TraceFile(
    val path: String,
    val conversations: List<Conversation>
)

/**
 * Conversation representing an AI code generation session
 * 
 * @property url URL to retrieve full conversation context
 * @property contributor Contributor information (AI, human, mixed)
 * @property ranges Line ranges generated in this conversation
 * @property related Related resources (sessions, prompts, etc.)
 * @property metadata Optional conversation metadata
 */
@Serializable
data class Conversation(
    val url: String,
    val contributor: Contributor,
    val ranges: List<Range>,
    val related: List<RelatedResource> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Contributor information
 * 
 * @property type Contributor type: ai, human, mixed, unknown
 * @property modelId AI model identifier (follows models.dev convention)
 *                   Format: provider/model-name (e.g., anthropic/claude-opus-4-5-20251101)
 * @property confidence Optional confidence score (0.0 to 1.0)
 */
@Serializable
data class Contributor(
    val type: String,
    @SerialName("model_id")
    val modelId: String? = null,
    val confidence: Double? = null
)

/**
 * Line range attribution
 * 
 * @property startLine Start line number (1-indexed)
 * @property endLine End line number (1-indexed, inclusive)
 * @property contentHash Hash of the content for tracking across refactors
 *                       Format: algorithm:hash (e.g., murmur3:9f2e8a1b)
 * @property contributor Optional override contributor for this specific range
 * @property metadata Optional range metadata
 */
@Serializable
data class Range(
    @SerialName("start_line")
    val startLine: Int,
    @SerialName("end_line")
    val endLine: Int,
    @SerialName("content_hash")
    val contentHash: String? = null,
    val contributor: Contributor? = null,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Related resource (session, prompt, etc.)
 * 
 * @property type Type of relation (session, prompt, parent, child, etc.)
 * @property url URL to the related resource
 * @property metadata Optional relation metadata
 */
@Serializable
data class RelatedResource(
    val type: String,
    val url: String,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Contributor types
 */
object ContributorType {
    const val AI = "ai"
    const val HUMAN = "human"
    const val MIXED = "mixed"
    const val UNKNOWN = "unknown"
}

/**
 * VCS types
 */
object VcsType {
    const val GIT = "git"
    const val JUJUTSU = "jj"
    const val MERCURIAL = "hg"
    const val SVN = "svn"
}

/**
 * Related resource types
 */
object RelationType {
    const val SESSION = "session"
    const val PROMPT = "prompt"
    const val PARENT = "parent"
    const val CHILD = "child"
    const val REFERENCE = "reference"
}
