package com.thoughtworks.archguard.report.infrastructure

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.rule.core.IssuePosition

@Serializable
data class TestSmellPO(
    val name: String,
    val fullName: String = "",
    val moduleName: String = "",
    val packageName: String = "",
    val typeName: String = "",
    val methodName: String = "",
    val detail: String,
    val position: String,
) {
    fun toTestSmellPO(): TestSmellPO {
        var split = fullName.split(":")
        val moduleName = "root"
        var packageName = ""
        var className = ""

        if (split.size >= 3) {
            className = split.last()
            split = split.dropLast(1)

            packageName = split.last()
        }

        val issuePosition = Json.decodeFromString<IssuePosition>(position)
        val methodName = issuePosition.additions.get("methodName").orEmpty()

        return TestSmellPO(name, fullName, moduleName, packageName, className, methodName, detail, position)
    }
}
