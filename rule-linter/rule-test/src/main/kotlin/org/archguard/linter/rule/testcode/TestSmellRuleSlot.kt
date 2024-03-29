package org.archguard.linter.rule.testcode

import chapi.domain.core.CodeDataStruct
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.OutputType
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import java.io.File

class TestSmellRuleSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = Issue.Companion::class.java.name

    override fun ticket(): Coin {
        return listOf(CodeDataStruct::class.java.name)
    }

    override fun prepare(items: List<Any>): List<Any> {
        val ruleSets = listOf(TestSmellRuleSetProvider().get())
        this.material = ruleSets
        return ruleSets
    }

    override fun process(items: List<Any>): OutputType {
        val testPath = "src" + File.separatorChar + "test"
        val dataStructs = (items as List<CodeDataStruct>).filter {
            it.FilePath.contains(testPath)
        }

        return TestSmellRuleVisitor(dataStructs).visitor(this.material as Iterable<RuleSet>)
    }
}