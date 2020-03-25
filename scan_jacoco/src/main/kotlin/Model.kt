package com.thoghtworks.archguard.scan_jacoco

@Sql("coverage")
data class Coverage(
        @Sql("instruction_missed") val instructionMissed: Int,
        @Sql("instruction_covered") val instructionCovered: Int,
        @Sql("line_missed") val lineMissed: Int,
        @Sql("line_covered") val lineCovered: Int,
        @Sql("branch_missed") val branchMissed: Int,
        @Sql("branch_covered") val branchCovered: Int,
        @Sql("complexity_missed") val complexityMissed: Int,
        @Sql("complexity_covered") val complexityCovered: Int,
        @Sql("method_missed") val methodMissed: Int,
        @Sql("method_covered") val methodCovered: Int,
        @Sql("class_missed") val classMissed: Int,
        @Sql("class_covered") val classCovered: Int,
        @Sql("project") val project: String,
        @Sql("scan_time") val sacnTime: Long
)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)