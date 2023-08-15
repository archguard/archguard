package org.archguard.scanner.core.diffchanges

import chapi.domain.core.CodeDataStruct

const val SHORT_ID_LENGTH = 7

open class NodeRelationBuilder {
    private val functionMap: MutableMap<String, Boolean> = mutableMapOf()
    private val reverseCallMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    private var loopCount: Int = 0
    private var lastReverseCallChild: String = ""

    /**
     * Calculate the relations between the functions of the two nodes.
     *
     * @param sourceNode The source node.
     * @param targetNode The target node.
     * @return The relations between the functions of the two nodes.
     * usage example:
     *
     * ```kotlin
     * val callName = packageName + "." + className + "." + functionName
     * val changeRelations: MutableList<NodeRelation> = mutableListOf()
     * calculateReverseCalls(callName, changeRelations, loopDepth) ?: listOf()
     * ```
     *
     */
    open fun calculateReverseCalls(
        sourceFunctionName: String,
        nodeRelations: MutableList<NodeRelation>,
        loopDepth: Int = SHORT_ID_LENGTH
    ): List<NodeRelation>? {
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
                val optRelations = calculateReverseCalls(child, nodeRelations, loopDepth)
                if (optRelations != null) {
                    nodeRelations += optRelations
                }
            }

            if (child != sourceFunctionName) {
                nodeRelations += NodeRelation(child, sourceFunctionName)
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
