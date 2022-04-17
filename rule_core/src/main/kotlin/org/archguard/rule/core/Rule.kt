package org.archguard.rule.core

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction

enum class Severity {
    // ERROR -> BLOCKER can be for continuous integration
    HINT, WARN, INFO, BLOCKER
}

enum class RuleType {
    /*
     * Normal code smell
     */
    CODE_SMELL,

    /*
     * REST API Smell
     */
    REST_API_SMELL,

    /*
     * Database smell, if a Controller have to many smells
     */
    DATABASE_MAP_SMELL,

    /*
     * SCM (like Git) change's smell, maybe change multiple files
     */
    CHANGE_SMELL,

    /*
     * for metric team size and others
     */
    ORGANIZATION,
}

typealias SmellEmit = (message: String) -> Unit

abstract class Rule(
    var key: String = "",
    // rule name for identify
    var name: String = "",
    // todo: i18N support, load from properties
    //  description for rule index
    var description: String = "",
    // message about context, can be information for refactor
    var message: String = "",
    // Rule Type, like Test, API, such as this
    var type: String = "",
    // like Priority in sonar
    var severity: Severity = Severity.INFO,
    // rule flags
    // given as a selector?
    var given: List<String> = arrayListOf(),
    // then as a functions?
    var status: String = "READY",
    // custom for search
    var tags: List<String> = listOf()
) {
    open fun visit(rootNode: CodeDataStruct, callback: SmellEmit) {}
}

abstract class IfttRule : Rule() {
    fun given(conditions: List<String>): IfttRule {
        return this
    }

    fun then(conditions: List<String>): IfttRule {
        return this
    }
}