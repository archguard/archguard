package org.archguard.dsl.insight

import org.archguard.dsl.context

class InsightDecl() {
    private var _field: String = ""
    private var _name: String = ""
    private var _version: String = ""
    private var _comparison: String = ""

    fun comparison(comparisonStr: String) {
        this._comparison = comparisonStr
    }

    fun version(versionStr: String) {
        this._version = versionStr
    }

    fun field(typeStr: String) {
        this._field = typeStr
    }

    fun name(name: String) {
        this._name = name
    }
}

fun insight(init: InsightDecl.() -> Unit): InsightDecl {
    val insightDecl = InsightDecl()
    insightDecl.init()
    context.insight = insightDecl
    return insightDecl
}