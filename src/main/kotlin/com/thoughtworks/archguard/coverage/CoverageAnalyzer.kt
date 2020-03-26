package com.thoughtworks.archguard.coverage

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class CoverageAnalyzer(val jdbi: Jdbi) {

    fun analyzeExecFile(): Bundle {
        return jdbi.withHandle<Bundle, Exception> {
            val queryCoverage = """
|               select 
|                   instruction_missed ,instruction_covered , 
                    line_missed ,line_covered , 
                    branch_missed , branch_covered , 
                    complexity_missed , complexity_covered , 
                    method_missed ,method_covered , 
                    class_missed , class_covered , 
                    bundle_name, scan_time
|               from bundle 
|               order by scan_time desc
|               limit 1
|               """.trimMargin()


            it.createQuery(queryCoverage)
                    .mapToBean(Bundle::class.java)
                    .findFirst().orElse(Bundle())
        }

    }

}

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

