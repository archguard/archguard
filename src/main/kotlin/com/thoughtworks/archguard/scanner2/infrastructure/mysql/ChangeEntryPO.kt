package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.ChangeEntry

class ChangeEntryPO(
    val changeMode: String,
    val cognitiveComplexity: Int,
    val commitId: String,
    val commitTime: Long,
    val newPath: String,
    val oldPath: String
) {
    fun toChangeEntry(): ChangeEntry {
        return ChangeEntry(oldPath, newPath, cognitiveComplexity, commitTime, commitId, changeMode)
    }
}
