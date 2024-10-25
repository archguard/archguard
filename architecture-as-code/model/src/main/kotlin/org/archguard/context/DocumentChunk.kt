package org.archguard.context

import kotlinx.serialization.Serializable

@Serializable
data class DocumentChunk(
    val filename: String = "",
    val fileUri: String = "",
    val type: String = "",
    val title: String = "",
    val author: String = "",
    val chunk: String = "",
    val startLine: Long = 0,
    val endLine: Long = 0,
    val chunkIndex: Long = 0,
)
