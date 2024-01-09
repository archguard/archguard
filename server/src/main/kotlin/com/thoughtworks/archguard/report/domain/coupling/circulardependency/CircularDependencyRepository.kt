package com.thoughtworks.archguard.report.domain.coupling.circulardependency

import org.archguard.smell.BadSmellLevel

interface CircularDependencyRepository {
    fun getCircularDependency(systemId: Long, type: CircularDependencyType, limit: Long, offset: Long): List<String>
    fun getCircularDependencyCount(systemId: Long, type: CircularDependencyType): Long
    fun getCircularDependencyBadSmellCalculateResult(
        systemId: Long,
        type: CircularDependencyType,
        thresholdRanges: Array<LongRange>
    ): BadSmellLevel
}
