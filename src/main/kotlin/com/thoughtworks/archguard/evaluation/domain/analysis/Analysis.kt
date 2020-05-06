package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report

interface Analysis {
    fun getName(): String
    fun getQualityReport(): Report?
}