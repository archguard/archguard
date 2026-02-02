package org.archguard.linter.rule.protobuf

import chapi.domain.core.CodeDataStruct
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet

class ProtobufRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        // SourceCodeWorker feeds CodeDataStruct (including `.proto`) to SlotHub.
        return listOf(CodeDataStruct::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(ProtobufRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): List<Any> {
        @Suppress("UNCHECKED_CAST")
        return ProtobufRuleVisitor(items as List<CodeDataStruct>).visitor(this.material as Iterable<RuleSet>)
    }
}