package org.archguard.dsl.insight

import org.archguard.dsl.context

class InsightDecl() {
    private var _field: String = ""
    private var _name: String = ""
    private var _condition: String = ""

    fun condition(comparisonStr: String) {
        this._condition = comparisonStr
    }

    fun name(name: String) {
        this._name = name
    }

    fun field(type: String) {
        this._field = type
    }

    fun field(type: String, string: String) {
        this._field = type
    }
}

fun insight(init: InsightDecl.() -> Unit): InsightDecl {
    val insightDecl = InsightDecl()
    insightDecl.init()
    context.insight = insightDecl
    return insightDecl
}
