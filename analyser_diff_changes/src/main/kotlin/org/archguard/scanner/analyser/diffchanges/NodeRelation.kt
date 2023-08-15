package org.archguard.scanner.analyser.diffchanges

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.diffchanges.ChangeRelation

open class NodeRelation {
    protected val functionMap: MutableMap<String, Boolean> = mutableMapOf()
    protected val reverseCallMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var loopCount: Int = 0
    private var lastReverseCallChild: String = ""

    protected fun calculateReverseCalls(
        sourceFunctionName: String,
        changeRelations: MutableList<ChangeRelation>,
        loopDepth: Int
    ): List<ChangeRelation>? {
        if (loopCount > loopDepth) {
            return null
        }

        loopCount++

        val calls = reverseCallMap[sourceFunctionName]
        calls?.forEach { child ->
            if (child == lastReverseCallChild) {
                return null
            }

            if (reverseCallMap[child] != null) {
                lastReverseCallChild = child
                val optRelations = calculateReverseCalls(child, changeRelations, loopDepth)
                if (optRelations != null) {
                    changeRelations += optRelations
                }
            }

            if (child != sourceFunctionName) {
                changeRelations += ChangeRelation(child, sourceFunctionName)
            }
        }

        return null
    }

    protected fun fillFunctionMap(dataStructs: List<CodeDataStruct>) {
        dataStructs.forEach { node ->
            node.Functions.forEach {
                functionMap[node.Package + "." + node.NodeName + "." + it.Name] = true
            }
        }
    }

    protected fun fillReverseCallMap(dataStructs: List<CodeDataStruct>) {
        dataStructs.forEach { node ->
            node.Fields.forEach {
                it.Calls.forEach {
                    // todo: add support for field call
                }
            }

            node.Functions.forEach {
                val caller = node.Package + "." + node.NodeName + "." + it.Name
                it.FunctionCalls.forEach { codeCall ->
                    val callee = codeCall.buildFullMethodName()
                    if (functionMap[callee] != null) {
                        if (reverseCallMap[callee] == null) {
                            reverseCallMap[callee] = mutableListOf()
                        }

                        reverseCallMap[callee]!! += caller
                    }
                }
            }
        }
    }

}
