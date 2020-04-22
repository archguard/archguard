package com.thoughtworks.archguard.report.domain.model

enum class Dimension {
    INSTRUCTION, LINE, BRANCH, COMPLEXITY, METHOD, CLASS;

    fun rateSnippet(): String {
        val dms = this.toString().toLowerCase()
        return "${dms}_covered*1.00/(${dms}_covered+${dms}_missed)"
    }
}