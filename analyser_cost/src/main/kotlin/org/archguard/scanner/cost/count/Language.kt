package org.archguard.scanner.cost.count

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val start: String,
    val end: String,
    val ignoreEscape: Boolean?,
    val docString: Boolean?
)

@Serializable
data class Language(
    val name: String,
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
    val complexity: Trie? = null,
    @SerialName("multi_line_comments")
    val multiLineComments: Trie? = null,
    @SerialName("single_line_comments")
    val singleLineComments: Trie? = null,
    val strings: Trie? = null,
    val tokens: Trie? = null,
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