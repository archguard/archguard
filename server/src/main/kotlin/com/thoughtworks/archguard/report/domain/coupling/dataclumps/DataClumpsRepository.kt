package com.thoughtworks.archguard.report.domain.coupling.dataclumps

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateResult

interface DataClumpsRepository {
    fun getLCOM4AboveThresholdCount(systemId: Long, dataClumpsLCOM4Threshold: Int): Long
    fun getLCOM4AboveThresholdList(systemId: Long, dataClumpsLCOM4Threshold: Int, limit: Long, offset: Long): List<ClassDataClump>
    fun getLCOM4AboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellCalculateResult
}
