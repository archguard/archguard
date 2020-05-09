package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.model.Bundle
import com.thoughtworks.archguard.report.domain.model.Dimension
import com.thoughtworks.archguard.report.domain.model.TopItem
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class CoverageRepo(val jdbi: Jdbi) {

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

    fun getAllBundles(): List<Bundle> {
        return jdbi.withHandle<List<Bundle>, Exception> {
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
|               """.trimMargin()


            it.createQuery(queryCoverage)
                    .mapToBean(Bundle::class.java)
                    .list()
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
            it.createQuery(sql)
                    .bind("n", n)
                    .mapToBean(TopItem::class.java).list()
        }
    }

    fun getClassCoverageByFiles(files: List<String>): List<Bundle> {
        return jdbi.withHandle<List<Bundle>, Exception> {
            val sql = """
                select bundle_name, class_missed, class_covered 
                from item where item_name in (${files.joinToString("','", "'", "'")})
                """.trimIndent()
            it.createQuery(sql)
                    .mapToBean(Bundle::class.java)
                    .list()
        }
    }

    fun getClassCoverageByBundle(files: List<String>): List<Bundle> {
        return jdbi.withHandle<List<Bundle>, Exception> {
            val sql = """
                select bundle_name, class_missed, class_covered 
                from item where item_type='PACKAGE' and bundle_name in (${files.joinToString("','", "'", "'")})
                """.trimIndent()
            it.createQuery(sql)
                    .mapToBean(Bundle::class.java)
                    .list()
        }
    }
}