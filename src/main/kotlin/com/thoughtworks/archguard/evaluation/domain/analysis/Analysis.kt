package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report

interface Analysis {
    fun getQualityReport(): Report?
    fun getName(): String
}