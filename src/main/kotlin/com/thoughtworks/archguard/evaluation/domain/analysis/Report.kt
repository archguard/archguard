package com.thoughtworks.archguard.evaluation.domain.analysis

interface Report {
    fun getImprovements(): List<String>
}
