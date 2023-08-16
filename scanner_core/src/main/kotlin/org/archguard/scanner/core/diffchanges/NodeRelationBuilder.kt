package org.archguard.scanner.core.diffchanges

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction

const val SHORT_ID_LENGTH = 7

open class NodeRelationBuilder {
    open val functionMap: MutableMap<String, Boolean> = mutableMapOf()
    open val reverseCallMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    open val injectionMap: MutableMap<String, String> = mutableMapOf()

    private var loopCount: Int = 0
    private var lastReverseCallChild: String = ""

    open fun resetCount() {
        loopCount = 0
    }

    /**
     * Calculate the relations between the functions of the two nodes.
     * <b>Before calling this function, you need to call [fillFunctionMap] and [fillReverseCallMap] </b>
     *
     * @param sourceNode The source node.
     * @param targetNode The target node.
     * @return The relations between the functions of the two nodes.
     *
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

        val maybeNewFuncName = updateInjectionNode(sourceFunctionName)
        val calls = reverseCallMap[maybeNewFuncName]
        calls?.forEach { child ->
            if (child == lastReverseCallChild) {
                return null
            }

            if (reverseCallMap[child] != null) {
                lastReverseCallChild = child
                val sourceName = updateInjectionNode(child)
                val optRelations = calculateReverseCalls(sourceName, nodeRelations, loopDepth)
                if (optRelations != null) {
                    nodeRelations += optRelations
                }
            }

            if (child != maybeNewFuncName) {
                val sourceName = updateInjectionNode(child)
                val targetName = updateInjectionNode(maybeNewFuncName)
                nodeRelations += NodeRelation(sourceName, targetName)
            }
        }

        return null
    }

    /**
     * @canonicalName will be xxx.xxx.xxx.NodeName.FunctionName,
     */
    private fun updateInjectionNode(canonicalName: String): String {
        val functionName = canonicalName.substringAfterLast(".")
        val nodeName = canonicalName.substringBeforeLast(".")

        val injectionName = injectionMap[nodeName]
        if (injectionName != null) {
            return "$injectionName.$functionName"
        }

        return canonicalName
    }

    open fun fillFunctionMap(dataStructs: List<CodeDataStruct>) {
        dataStructs.forEach { node ->
            updateDependencyInjection(node)
            node.Functions.forEach {
                functionMap[node.Package + "." + node.NodeName + "." + it.Name] = true
            }
        }
    }

    open fun fillReverseCallMap(dataStructs: List<CodeDataStruct>) {
        dataStructs.forEach { node ->
            updateDependencyInjection(node)

            node.Functions.forEach {
                insertToReverse(node, node.NodeName, it)
            }
        }
    }

    private fun insertToReverse(
        node: CodeDataStruct,
        nodeName: String,
        it: CodeFunction
    ) {
        val caller = node.Package + "." + nodeName + "." + it.Name
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

    private fun updateDependencyInjection(node: CodeDataStruct) {
        val hasInjectionAnnotation =
            node.Annotations.find {
                it.Name == "Service" || it.Name == "Component" || it.Name == "Repository"
            }

        if (hasInjectionAnnotation != null && node.Implements.isNotEmpty()) {
            val canonicalName = node.Package + "." + node.NodeName

            node.Implements.find { node.NodeName == it.substringAfterLast(".") + "Impl" }?.let {
                injectionMap[canonicalName] = it
            }
        }
    }
}
