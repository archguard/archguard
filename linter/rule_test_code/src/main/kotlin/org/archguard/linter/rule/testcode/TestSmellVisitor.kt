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

class TestSmellContext(val methodMap: MutableMap<String, CodeFunction>) : RuleContext() {}

class TestSmellVisitor(private val structs: Array<CodeDataStruct>) : RuleVisitor {
    private var context: TestSmellContext

    init {
        this.context = TestSmellContext(this.buildCallMethodMap(this.structs))
    }

    private fun buildCallMethodMap(nodes: Array<CodeDataStruct>): MutableMap<String, CodeFunction> {
        val callMethodMap: MutableMap<String, CodeFunction> = mutableMapOf()
        for (node in nodes) {
            for (method in node.Functions) {
                callMethodMap[method.buildFullMethodName(node)] = method
            }
        }

        return callMethodMap
    }

    fun visitor(ruleSets: Iterable<RuleSet>, rootNode: CodeDataStruct): Array<Issue> {
        var results: Array<Issue> = arrayOf()
        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach {
                it.visit(rootNode, this.context, fun(rule: Rule, position: IssuePosition) {
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

        return results
    }
}
