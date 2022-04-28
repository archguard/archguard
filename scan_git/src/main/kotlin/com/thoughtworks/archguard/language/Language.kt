package com.thoughtworks.archguard.language

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