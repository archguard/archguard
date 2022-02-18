package com.thoughtworks.archguard.report_bak.domain.model

data class ChangeEntry(
        val newPath: String,
        val cognitiveComplexity: Int,
        val mode: String,
        val prvsCmtId: String?,
        val prvsCgnCmplxty: Int) {
    constructor() : this("", 0, "", null, 0)
}