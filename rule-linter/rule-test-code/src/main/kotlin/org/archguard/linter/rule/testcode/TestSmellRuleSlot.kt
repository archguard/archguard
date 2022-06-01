package org.archguard.linter.rule.testcode

import chapi.domain.core.CodeDataStruct
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.OutputType
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet

class TestSmellRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(CodeDataStruct.Companion::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(TestSmellRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): OutputType {
        return TestSmellRuleVisitor(items as List<CodeDataStruct>).visitor(this.material as Iterable<RuleSet>)
    }
}