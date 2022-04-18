package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.RuleResult
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleVisitor
import org.archguard.rule.core.SmellPosition

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

    fun visitor(ruleSets: Iterable<RuleSet>, rootNode: CodeDataStruct): Array<RuleResult> {
        var results: Array<RuleResult> = arrayOf()
        ruleSets.forEach { ruleSet ->
            ruleSet.rules.forEach {
                it.visit(rootNode, this.context, fun(rule: Rule, position: SmellPosition) {
                    results += RuleResult(
                        position,
                        ruleId = rule.key,
                        name = rule.name,
                        detail = rule.description
                    )
                })
            }
        }

        return results
    }
}
