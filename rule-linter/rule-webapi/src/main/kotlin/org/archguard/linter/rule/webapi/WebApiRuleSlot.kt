package org.archguard.linter.rule.webapi

import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.scanner.core.sourcecode.ContainerResource

class WebApiRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(ContainerResource.Companion::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(WebApiRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): List<Any> {
        return WebApiRuleVisitor(items as List<ContainerResource>).visitor(this.material as Iterable<RuleSet>)
    }
}