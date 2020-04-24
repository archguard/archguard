package com.thoughtworks.archguard.evaluation.domain.report

interface Report {
    fun getImprovements(): List<String>
}
