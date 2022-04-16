package org.archguard.rule.impl.tbs.rules

import org.archguard.rule.impl.tbs.TbsRule

class NoIgnoreTestRule : TbsRule() {
    init {
        this.name = "NoIgnoreTest"
        this.key = this.javaClass.name
        this.description = "Some casing description"
        // rule for AstPath or ognl?
        this.given = listOf("$.class.function.annotations contains 'Ignore'")
    }
}