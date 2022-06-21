package org.archguard.dsl.insight

import org.archguard.domain.insight.InsightFieldFilter
import org.archguard.domain.insight.InsightFilterType
import org.archguard.dsl.context

class InsightDecl() {
    private lateinit var _fieldCondition: InsightFieldFilter
    private var _field: String = ""
    private var _name: String = ""
    private var _condition: String = ""

    fun condition(comparisonStr: String) {
        this._condition = comparisonStr
    }

    fun name(name: String) {
        this._name = name
    }

    fun field(type: String, conditionFunc: () -> InsightFieldFilter) {
        this._field = type
        this._fieldCondition = conditionFunc()
    }

    fun field(type: String, string: String) {
        this._field = type
        this._fieldCondition = InsightFieldFilter(
            InsightFilterType.NORMAL,
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
