package com.thoughtworks.archguard.report.domain.dataclumps

interface DataClumpsRepository {
    fun getLCOM4AboveThresholdCount(systemId: Long, dataClumpsLCOM4Threshold: Int): Long
    fun getLCOM4AboveThresholdList(systemId: Long, dataClumpsLCOM4Threshold: Int, limit: Long, offset: Long): List<ClassDataClump>
}