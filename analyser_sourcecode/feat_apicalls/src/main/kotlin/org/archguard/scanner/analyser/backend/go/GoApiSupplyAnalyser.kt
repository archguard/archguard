package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.context.ContainerDemand
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.scanner.analyser.base.ApiAnalyser

class GoApiSupplyAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()

    private var apiGroupStack = ArrayDeque<String>()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        node.Functions.forEach {
            apiGroupStack = ArrayDeque()
            analysisFunctionCall(it)
        }
    }

    private fun analysisFunctionCall(function: CodeFunction): String? {
        function.FunctionCalls.forEachIndexed { index, funcCall ->
            if (isGinNode(funcCall.NodeName)) {
                handleForGin(funcCall, function)?.apply {
                    resources = resources + this
                }
            } else if (isMuxNode(funcCall.NodeName)) {
                val nextCall = function.FunctionCalls.getOrNull(index + 1)
                handleForMux(funcCall, function, nextCall).apply {
                    resources = resources + this
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

    private fun handleForGin(funcCall: CodeCall, function: CodeFunction): ContainerSupply? {
        if (funcCall.Parameters.isEmpty()) {
            return null
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

                return ContainerSupply(
                    sourceUrl = url,
                    sourceHttpMethod = funcCall.FunctionName,
                    packageName = function.Package,
                    className = "",
                    methodName = function.Name
                )
            }
        }

        return null
    }

    private fun isGinNode(nodeName: String): Boolean {
        val name = nodeName.removePrefix("*")
        return name == "gin" || name == "gin.Engine" || name == "gin.RouterGroup" || name == "gin.Default"
    }

    private fun isMuxNode(nodeName: String): Boolean {
        val name = nodeName.removePrefix("*")
        return name == "mux" || name == "mux.Router"
    }

    /**
     *
     * A typical example of a Go mux router is as follows:
     *
     * ```
     * func main() {
     *     r := mux.NewRouter()
     *     r.HandleFunc("/", HomeHandler).Methods("GET", "POST")
     *     r.HandleFunc("/products", ProductsHandler).Methods("POST")
     *     r.HandleFunc("/articles", ArticlesHandler).Methods("POST")
     *     http.Handle("/", r)
     * }
     * ```
     *
     * we should try to extract the url and http method from the mux router.
     *
     * the .Methods argument, Also can be `http.MethodGet`, `http.MethodPost`, `http.MethodPut`, `http.MethodDelete`
     */
    private fun handleForMux(funcCall: CodeCall, function: CodeFunction, nextCall: CodeCall?): List<ContainerSupply> {
        val maybeUrl = funcCall.Parameters.firstOrNull()?.TypeValue ?: ""

        /// get methods from next call
        var methods = mutableListOf<String>()
        if (nextCall != null) {
            methods = nextCall.Parameters.map {
                it.TypeValue
            }.toMutableList()
        }

        when (funcCall.FunctionName) {
            "HandleFunc" -> {
                return methods.map {
                    ContainerSupply(
                        sourceUrl = maybeUrl,
                        sourceHttpMethod = it,
                        packageName = function.Package,
                        className = "",
                        methodName = function.Name
                    )
                }
            }
        }

        return emptyList()
    }

    override fun toContainerServices(): List<ContainerService> {
        return mutableListOf(
            ContainerService(
                name = "",
                resources = resources,
                demands = demands
            )
        )
    }

    fun analysisDemands(input: List<CodeDataStruct>, path: String) {
        this.demands = GoProtobufConsumerAnalyser(input, path).analysis()
    }
}