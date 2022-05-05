package com.thoughtworks.archguard.scanner.jacoco


open class Coverage(
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
        @Sql("class_covered") val classCovered: Int
)

@Sql("bundle")
class Bundle(
        instructionMissed: Int,
        instructionCovered: Int,
        lineMissed: Int,
        lineCovered: Int,
        branchMissed: Int,
        branchCovered: Int,
        complexityMissed: Int,
        complexityCovered: Int,
        methodMissed: Int,
        methodCovered: Int,
        classMissed: Int,
        classCovered: Int,
        @Sql("bundle_name") val bundleName: String,
        @Sql("scan_time") val scanTime: Long
) : Coverage(instructionMissed, instructionCovered,
        lineMissed, lineCovered,
        branchMissed, branchCovered,
        complexityMissed, complexityCovered,
        methodMissed, methodCovered,
        classMissed, classCovered)

/*represent coverage of  package, file, class and method */
@Sql("item")
class Item(instructionMissed: Int, instructionCovered: Int,
           lineMissed: Int, lineCovered: Int,
           branchMissed: Int, branchCovered: Int,
           complexityMissed: Int, complexityCovered: Int,
           methodMissed: Int, methodCovered: Int,
           classMissed: Int, classCovered: Int,
           @Sql("item_name") val itemName: String,
           @Sql("Item_type") val itemType: ItemType,
           @Sql("bundle_name") val bundleName: String, @Sql("scan_time") val scanTime: Long
) : Coverage(instructionMissed, instructionCovered,
        lineMissed, lineCovered,
        branchMissed, branchCovered,
        complexityMissed, complexityCovered,
        methodMissed, methodCovered,
        classMissed, classCovered)


enum class ItemType {
    PACKAGE, FILE
}

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)