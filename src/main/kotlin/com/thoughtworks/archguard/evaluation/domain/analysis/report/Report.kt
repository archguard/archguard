package com.thoughtworks.archguard.evaluation.domain.analysis.report

interface Report {
    fun getImprovements(): List<String>
}
