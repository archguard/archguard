package com.thoughtworks.archguard.report.domain.coupling.dataclumps

import org.archguard.smell.BadSmellResult

interface DataClumpsRepository {
    fun getLCOM4AboveThresholdCount(systemId: Long, dataClumpsLCOM4Threshold: Int): Long
    fun getLCOM4AboveThresholdList(systemId: Long, dataClumpsLCOM4Threshold: Int, limit: Long, offset: Long): List<ClassDataClump>
    fun getLCOM4AboveBadSmellCalculateResult(systemId: Long, thresholdRanges: Array<LongRange>): BadSmellResult
}
