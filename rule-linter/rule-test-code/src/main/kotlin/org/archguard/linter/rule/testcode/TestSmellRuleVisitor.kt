package org.archguard.linter.rule.testcode

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleType

class TestSmellContext(val methodMap: MutableMap<String, CodeFunction>) : RuleContext()

class TestSmellRuleVisitor(private val structs: List<CodeDataStruct>) : RuleVisitor(structs) {
    private var context: TestSmellContext

    init {
        this.context = TestSmellContext(this.buildCallMethodMap(this.structs))
    }

    private fun buildCallMethodMap(nodes: List<CodeDataStruct>): MutableMap<String, CodeFunction> {
        val callMethodMap: MutableMap<String, CodeFunction> = mutableMapOf()
        for (node in nodes) {
            for (method in node.Functions) {
                callMethodMap[method.buildFullMethodName(node)] = method
            }
        }

        return callMethodMap
    }

    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()

        this.structs.forEach { struct ->
            ruleSets.forEach { ruleSet ->
                ruleSet.rules.forEach {
                    it.visit(struct, this.context, fun(rule: Rule, position: IssuePosition) {
                        results += Issue(
                            position,
                            ruleId = rule.key,
                            name = rule.name,
                            detail = rule.description,
                            ruleType = RuleType.TEST_CODE_SMELL
                        )
                    })
                }
            }
        }

        return results
    }
}
