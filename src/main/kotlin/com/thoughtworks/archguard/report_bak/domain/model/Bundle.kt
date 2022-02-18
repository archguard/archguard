package com.thoughtworks.archguard.report_bak.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class Bundle(
        var bundleName: String,
        var itemName: String,
        var classMissed: Int,
        var classCovered: Int,
        @JsonIgnore
        var instructionMissed: Int,
        @JsonIgnore
        var instructionCovered: Int,
        @JsonIgnore
        var lineMissed: Int,
        @JsonIgnore
        var lineCovered: Int,
        @JsonIgnore
        var branchMissed: Int,
        @JsonIgnore
        var branchCovered: Int,
        @JsonIgnore
        var complexityMissed: Int,
        @JsonIgnore
        var complexityCovered: Int,
        @JsonIgnore
        var methodMissed: Int,
        @JsonIgnore
        var methodCovered: Int,
        @JsonIgnore
        var scanTime: Long) {
    constructor() : this("", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    fun calculateClassCoveredPercent(): Double {
        return if (classCovered.plus(classMissed) < 1) {
            0.0
        } else {
            classCovered.toDouble().div(classCovered.plus(classMissed))
        }
    }
}