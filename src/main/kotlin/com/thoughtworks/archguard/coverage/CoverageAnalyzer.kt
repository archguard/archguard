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


    fun countRateBetween(dmsType: Dimension, left: Float, right: Float): Int {
        return jdbi.withHandle<Int, Exception> {
            val rate = dmsType.rateSnippet()
            val sql = "select count(*) from item where $rate>=:left and $rate<:right and item_type='FILE'"
            it.createQuery(sql)
                    .bind("left", left)
                    .bind("right", right)
                    .mapTo(Int::class.java).one()
        }
    }

    fun top(dmsType: Dimension, n: Int): List<TopItem> {

        return jdbi.withHandle<List<TopItem>, Exception> {
            val sql = """
                select item_name, ${dmsType.rateSnippet()} coverageRate 
                from item where item_type='FILE'
                order by coverageRate desc limit :n """.trimIndent()
            println("sql = ${sql}")
            it.createQuery(sql)
                    .bind("n", n)
                    .mapToBean(TopItem::class.java).list()
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


data class TopItem(var itemName: String, var coverageRate: Float) {
    constructor() : this("", 0.0F)
}

enum class Dimension {
    INSTRUCTION, LINE, BRANCH, COMPLEXITY, METHOD, CLASS;

    fun rateSnippet(): String {
        val dms = this.toString().toLowerCase()
        return "${dms}_covered*1.00/(${dms}_covered+${dms}_missed)"
    }
}