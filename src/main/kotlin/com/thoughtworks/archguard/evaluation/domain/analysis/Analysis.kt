package com.thoughtworks.archguard.evaluation.domain.analysis

interface Analysis {
    fun getQualityReport(): Report
    fun getName(): String
}