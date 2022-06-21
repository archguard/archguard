package org.archguard.dsl.insight

import org.archguard.dsl.context

enum class InsightConditionType {
    // can be String
    NORMAL,
    REGEXP,
}

class InsightCondition(
    val type: InsightConditionType = InsightConditionType.REGEXP,
    val value: String = "",
)

fun regexp(value: String): InsightCondition {
    return InsightCondition(InsightConditionType.REGEXP, value)
}

data class InsightModel(
    val condType: InsightConditionType,
    val field: String,
    val comparison: String,
    val value: String,
)

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

    fun field(type: String, conditionFunc: () -> InsightCondition) {
        this._field = type
        this._fieldCondition = conditionFunc()
    }

    fun field(type: String, string: String) {
        this._field = type
        this._fieldCondition = InsightCondition(
            InsightConditionType.NORMAL,
            string
        )
    }
}

fun insight(init: InsightDecl.() -> Unit): InsightDecl {
    val insightDecl = InsightDecl()
    insightDecl.init()
    context.insight = insightDecl
    return insightDecl
}
