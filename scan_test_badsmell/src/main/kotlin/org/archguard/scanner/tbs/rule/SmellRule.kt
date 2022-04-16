package org.archguard.scanner.tbs.rule

enum class Severity {
    // ERROR -> BLOCKER can be for continuous integration
    HINT, WARN, INFO, BLOCKER
}

open class Rule(
    val key: String = "",
    // rule name for identify
    val name: String = "",
    //  description for rule index
    val description: String = "",
    // message about context, can be information for refactor
    val message: String = "",
    // Rule Type, like Test, API, such as this
    val type: String = "",
    // like Priority in sonar
    val severity: Severity = Severity.INFO,
    // rule flags
    val status: String = "READY",
    // custom for search
    val tags: List<String> = listOf()
) {}

abstract class IfttRule: Rule() {
    fun given(conditions: List<String>): IfttRule {
        return this
    }
    fun then(conditions: List<String>): IfttRule {
        return this
    }
}