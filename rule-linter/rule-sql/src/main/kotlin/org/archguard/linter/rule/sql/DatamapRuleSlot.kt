package org.archguard.linter.rule.sql

import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.OutputType
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.model.CodeDatabaseRelation

class DatamapRuleSlot() : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(CodeDatabaseRelation::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(SqlRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): OutputType {
        return DatamapRuleVisitor(items as List<CodeDatabaseRelation>).visitor(this.material as Iterable<RuleSet>)
    }
}