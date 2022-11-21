package org.archguard.scanner.cost.count

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LanguageSummary(
    var name: String = "",
    var bytes: Long = 0,
    var codeBytes: Long = 0,
    var lines: Long = 0,
    var code: Long = 0,
    var comment: Long = 0,
    var blank: Long = 0,
    var complexity: Long = 0,
    var count: Long = 0,
    var weightedComplexity: Double = 0.0,
    // we don't need to serialise this
    @Transient
    var files: List<FileJob> = listOf(),
)

@Serializable
data class Quote(
    val start: String,
    val end: String,
    val ignoreEscape: Boolean = false,
    val docString: Boolean = false
)

@Serializable
data class Language(
    var name: String? = null,
    @SerialName("line_comment")
    val lineComment: List<String>? = listOf(),
    @SerialName("complexitychecks")
    val complexityChecks: List<String>? = listOf(),
    val extensions: List<String> = listOf(),
    val extensionFile: Boolean? = false,
    @SerialName("multi_line")
    val multiLine: List<List<String>>? = listOf(),
    val quotes: List<Quote>? = listOf(),
    @SerialName("nestedmultiline")
    val nestedMultiLine: Boolean? = false,
    val keywords: List<String>? = listOf(),
    @SerialName("filenames")
    val fileNames: List<String>? = listOf(),
    @SerialName("shebangs")
    val sheBangs: List<String>? = listOf(),
)

@Serializable
data class LanguageFeature(
    var complexity: Trie? = null,
    @SerialName("multi_line_comments")
    var multiLineComments: Trie? = null,
    @SerialName("single_line_comments")
    var singleLineComments: Trie? = null,
    var strings: Trie? = null,
    var tokens: Trie? = null,
    val nested: Boolean? = false,
    @SerialName("complexity_check_mask")
    val complexityCheckMask: Byte? = 0,
    @SerialName("single_line_comment_mask")
    val singleLineCommentMask: Byte? = 0,
    @SerialName("multi_line_comment_mask")
    val multiLineCommentMask: Byte? = 0,
    @SerialName("string_check_mask")
    val stringCheckMask: Byte? = 0,
    @SerialName("process_mask")
    val processMask: Byte? = 0,
    val keywords: List<String>? = listOf(),
    val quotes: List<Quote>? = listOf(),
)