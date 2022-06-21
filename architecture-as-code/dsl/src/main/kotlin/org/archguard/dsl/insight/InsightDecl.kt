package org.archguard.dsl.insight

import org.archguard.dsl.context

class InsightCondition(
    val type: String = "regexp",
    val value: String = "",
)

fun regexp(value: String): InsightCondition {
    return InsightCondition("regex", value)
}

class InsightDecl() {
    private lateinit var _fieldCondition: InsightCondition
    private var _field: String = ""
    private var _name: String = ""
    private var _condition: String = ""

    fun condition(comparisonStr: String) {
        this._condition = comparisonStr
    }

    fun name(name: String) {
        this._name = name
    }

    fun field(typeStr: String, function: () -> InsightCondition) {
        this._field = typeStr
        this._fieldCondition = function()
    }
}

fun insight(init: InsightDecl.() -> Unit): InsightDecl {
    val insightDecl = InsightDecl()
    insightDecl.init()
    context.insight = insightDecl
    return insightDecl
}