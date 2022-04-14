package com.thoughtworks.archguard.report.domain.badsmell

interface ThresholdSuiteRepository {
    fun getAllBadSmellThresholdSuites(): List<ThresholdSuite>
}
