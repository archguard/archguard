package org.archguard.rule.impl.tbs.rules

import org.archguard.rule.impl.tbs.TbsRule

class UnknownTestRule : TbsRule() {
    init {
        this.name = "UnknownTest"
        this.key = this.javaClass.name
        this.description = "test not assert"
        // rule for AstPath or ognl?
        this.given = listOf("$.class.function.calls <= 0")
    }

    fun byLanguage() {

    }
}