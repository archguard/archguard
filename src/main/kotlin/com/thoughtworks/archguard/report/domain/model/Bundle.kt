package com.thoughtworks.archguard.report.domain.model

data class Bundle(
        var instructionMissed: Int,
        var instructionCovered: Int,
        var lineMissed: Int,
        var lineCovered: Int,
        var branchMissed: Int,
        var branchCovered: Int,
        var complexityMissed: Int,
        var complexityCovered: Int,
        var methodMissed: Int,
        var methodCovered: Int,
        var classMissed: Int,
        var classCovered: Int,
        var bundleName: String,
        var scanTime: Long
) {
    constructor() : this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "no info in db", 0)
}