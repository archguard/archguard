package com.thoughtworks.archguard.report.domain.badsmell

import org.archguard.threshold.ThresholdSuite

interface ThresholdSuiteRepository {
    fun getAllBadSmellThresholdSuites(): List<ThresholdSuite>
}
