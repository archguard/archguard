package com.thoughtworks.archguard.coverage

import org.springframework.stereotype.Component

@Component
class JacocoAnalyzer {

    fun analyzeExecFile(): Coverage {
        return Coverage(1,2,3,4,5,6,7,8,9,10,11,12)
    }

}

data class Coverage(
        val instructionMissed: Int,
        val instructionCovered: Int,
        val lineMissed: Int,
        val lineCovered: Int,
        val branchMissed: Int,
        val branchCovered: Int,
        val complexityMissed: Int,
        val complexityCovered: Int,
        val methodMissed: Int,
        val methodCovered: Int,
        val classMissed: Int,
        val classCovered: Int
)