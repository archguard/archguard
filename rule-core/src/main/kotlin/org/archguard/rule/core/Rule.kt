package org.archguard.rule.core

typealias IssueEmit = (rule: Rule, position: IssuePosition) -> Unit
typealias RuleContext = Any

/**
 * **Rule** is a abstract class
 */
open class Rule(
    // for lazy load
    var id: String = "",
    // class name
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
    // todo: like BestPractise, PossibleErrors
    var tags: List<String> = listOf(),
) {
    open fun visit(rootNode: Any, context: RuleContext, callback: IssueEmit) {}
}


enum class Severity {
    // ERROR -> BLOCKER can be for continuous integration
    HINT, WARN, INFO, BLOCKER
}

enum class RuleType {
    /*
     * Normal code smell
     */
    CODE_SMELL,

    // code smell for test
    TEST_CODE_SMELL,

    /*
     * REST API Smell
     */
    HTTP_API_SMELL,

    /*
     * Protobuf Smell
     */
    PROTOBUF_SMELL,

    /*
     * Database smell, if a Controller has to many smells
     */
    SQL_SMELL,

    /*
     * SCM (like Git) change's smell, maybe change multiple files
     */
    CHANGE_SMELL,

    /*
     * for metric team size and others
     */
    ORGANIZATION,

    /**
     * for layer architecture check
     */
    LAYER_SMELL,

    /**
     * Service layer check
     */
    SERVICE_SMELL,

    /**
     * for code comment
     */
    COMMENT_SMELL,

    /**
     * custom
     */
    CUSTOM
}
