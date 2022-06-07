package com.thoughtworks.archguard.report.infrastructure

import kotlinx.serialization.Serializable

@Serializable
data class TestSmellPO(
    val name: String,
    val fullName: String = "",
    val moduleName: String = "",
    val packageName: String = "",
    val typeName: String = "",
    val detail: String,
    val position: String,
) {
    fun toTestSmellPO(): TestSmellPO {
        var split = fullName.split(":")
        var moduleName = ""
        var packageName = ""
        var className = ""

        if (split.size >= 3) {
            className = split.last()
            split = split.dropLast(1)

            packageName = split.last()
            split = split.dropLast(1)

            moduleName = split.joinToString(":")
        }

        return TestSmellPO(name, fullName, moduleName, packageName, className, detail, position)
    }
}
