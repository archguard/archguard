package com.thoughtworks.archguard.report.domain.deepinheritance

interface DeepInheritanceRepository {
    fun getDitAboveThresholdCount(systemId: Long, threshold: Int): Long
    fun getDitAboveThresholdList(systemId: Long, threshold: Int, limit: Long, offset: Long): List<DeepInheritance>
}