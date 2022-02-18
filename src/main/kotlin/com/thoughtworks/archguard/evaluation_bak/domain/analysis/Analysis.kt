package com.thoughtworks.archguard.evaluation_bak.domain.analysis

import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.Report

interface Analysis {
    fun getName(): String
    fun getQualityReport(): Report?
}