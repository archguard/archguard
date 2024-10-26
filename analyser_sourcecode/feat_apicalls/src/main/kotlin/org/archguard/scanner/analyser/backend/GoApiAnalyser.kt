package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply

class GoApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var apiGroupStack = ArrayDeque<String>()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        node.Functions.forEach {
            apiGroupStack = ArrayDeque()
            analysisFunctionCall(it)
        }
    }

    private fun analysisFunctionCall(function: CodeFunction): String? {
        function.FunctionCalls.forEach { funcCall ->
            if (isGinNode(funcCall.NodeName)) {
                if (funcCall.Parameters.isEmpty()) {
                    return@forEach
                }

                val typeValue = funcCall.Parameters[0].TypeValue

                if (funcCall.FunctionName == "Group") {
                    apiGroupStack.add(typeValue)
                }

                when (funcCall.FunctionName) {
                    "GET", "POST", "PUT", "DELETE" -> {
                        val based = apiGroupStack.joinToString("")
                        val url = if (!based.endsWith("/") && !typeValue.startsWith("/")) {
                            "$based/$typeValue"
                        } else {
                            "$based$typeValue"
                        }

                        resources = resources + ContainerSupply(
                            sourceUrl = url,
                            sourceHttpMethod = funcCall.FunctionName,
                            packageName = function.Package,
                            className = "",
                            methodName = function.Name
                        )
                    }
                }
            }
        }

        if (function.InnerFunctions.isNotEmpty()) {
            function.InnerFunctions.forEach { innerFunc ->
                analysisFunctionCall(innerFunc)
            }
        }

        return null
    }

    private fun isGinNode(nodeName: String): Boolean {
        val name = nodeName.removePrefix("*")
        return name == "gin" || name == "gin.Engine" || name == "gin.RouterGroup" || name == "gin.Default"
    }

    override fun toContainerServices(): List<ContainerService> {
        return mutableListOf(
            ContainerService(
                name = "",
                resources = resources,
                demands = arrayListOf()
            )
        )
    }
}