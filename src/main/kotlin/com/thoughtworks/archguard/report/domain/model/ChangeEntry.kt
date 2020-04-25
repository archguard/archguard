package com.thoughtworks.archguard.report.domain.model

data class ChangeEntry(
        val newPath: String,
        val cognitiveComplexity: Int,
        val mode: String,
        val prvsCmtId: String?,
        val prvsCgnCmplxty: Int)