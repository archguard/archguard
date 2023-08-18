package org.archguard.scanner.core.document

import kotlinx.serialization.Serializable

@Serializable
data class DocumentLang(
    val filename: String = "",
    val chunk: String = "",
    val startLine: Long = 0,
    val endLine: Long = 0,
    val chunkIndex: Long = 0,
)
