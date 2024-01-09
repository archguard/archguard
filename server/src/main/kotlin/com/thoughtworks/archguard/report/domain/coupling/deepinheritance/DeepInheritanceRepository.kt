package com.thoughtworks.archguard.report.domain.coupling.deepinheritance

import org.archguard.smell.BadSmellResult

interface DeepInheritanceRepository {
    fun getDitAboveThresholdCount(systemId: Long, threshold: Int): Long
    fun getDitAboveThresholdList(systemId: Long, threshold: Int, limit: Long, offset: Long): List<DeepInheritance>
    fun getDitAboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellResult
}
