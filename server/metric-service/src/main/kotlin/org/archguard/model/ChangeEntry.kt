package org.archguard.model

class ChangeEntry(
    val oldPath: String,
    val newPath: String,
    val cognitiveComplexity: Int,
    val commitTime: Long,
    val commitId: String,
    val changeMode: String
)
