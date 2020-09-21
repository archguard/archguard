package com.thoughtworks.archgard.scanner2.domain.model

class ChangeEntry(val oldPath: String, val newPath: String, val cognitiveComplexity: Int,
                  val commitTime: Long, val commitId: String, val changeMode: String) {
}
