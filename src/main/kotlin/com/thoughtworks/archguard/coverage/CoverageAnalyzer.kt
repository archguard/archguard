package com.thoughtworks.archguard.coverage

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class CoverageAnalyzer(val jdbi: Jdbi) {

    fun analyzeExecFile(): Coverage {
        return jdbi.withHandle<Coverage, Exception> {
            val queryCoverage = """
|               select 
|                   instruction_missed as instructionMissed, 
|                   instruction_covered as instructionCovered, 
                    line_missed as lineMissed,
|                   line_covered as lineCovered, 
                    branch_missed as branchMissed, 
                    branch_covered as branchCovered, 
                    complexity_missed as complexityMissed, 
                    complexity_covered as complexityCovered, 
                    method_missed as methodMissed,
                    method_covered as methodCovered, 
                    class_missed as classMissed, 
                    class_covered as classCovered, 
                    project, 
                    scan_time as scanTime
|               from coverage 
|               order by scanTime desc
|               limit 1
|               """.trimMargin()


            it.createQuery(queryCoverage)
                    .mapToBean(Coverage::class.java)
                    .findFirst().orElse(Coverage())
        }

    }

}

data class Coverage(
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
        var project: String,
        var scanTime: Long
) {
    constructor() : this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "no info in db", 0)
}

