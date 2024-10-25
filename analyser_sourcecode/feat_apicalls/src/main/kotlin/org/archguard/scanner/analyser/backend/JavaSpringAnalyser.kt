package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerDemand
import org.archguard.scanner.core.sourcecode.ContainerSupply
import org.archguard.scanner.core.sourcecode.ContainerService

class JavaSpringAnalyser: ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        val routeAnnotation = node.filterAnnotations("RestController", "Controller", "RequestMapping")
        // 1. create resources
        if (routeAnnotation.isNotEmpty()) {
            var baseUrl = ""
            val mappingAnnotation = node.filterAnnotations("RequestMapping")
            if (mappingAnnotation.isNotEmpty() && mappingAnnotation[0].KeyValues.isNotEmpty()) {
                val url = mappingAnnotation[0].KeyValues[0].Value
                baseUrl = url.removePrefix("\"").removeSuffix("\"")
            }

            node.Functions.forEach { createResource(it, baseUrl, node) }
        }

        // 2. create demands
        val useRestTemplate = node.Imports.filter { it.Source.endsWith(".RestTemplate") }
        if (useRestTemplate.isNotEmpty()) {
            node.Functions.forEach { createDemand(it, node) }
        }
    }

    private fun createDemand(it: CodeFunction, node: CodeDataStruct) {
        it.FunctionCalls.forEach { call ->
            var functionName = call.FunctionName
            if (functionName.contains(".")) {
                functionName = functionName.split(".").last()
            }

            if (call.NodeName == "RestTemplate" && call.FunctionName != "<init>") {
                var method = ""
                val lowercase = functionName.lowercase()
                when {
                    lowercase.startsWith("get") -> {
                        method = "Get"
                    }
                    lowercase.startsWith("post") -> {
                        method = "Post"
                    }
                    lowercase.startsWith("delete") -> {
                        method = "Delete"
                    }
                    lowercase.startsWith("put") -> {
                        method = "Put"
                    }
                }

                var url = ""
                if (call.Parameters.isNotEmpty() && call.Parameters[0].TypeValue.isNotEmpty()) {
                    url = call.Parameters[0].TypeValue.removePrefix("\"").removeSuffix("\"")
                }

                if (method.isNotEmpty()) {
                    demands = demands + ContainerDemand(
                        source_caller = node.NodeName,
                        target_url = url,
                        target_http_method = method
                    )
                }
            }
        }
    }

    private fun createResource(func: CodeFunction, baseUrl: String, node: CodeDataStruct) {
        var httpMethod = ""
        var route = baseUrl
        for (annotation in func.Annotations) {
            var isHttpAnnotation = true
            when (annotation.Name) {
                "GetMapping" -> httpMethod = "Get"
                "PostMapping" -> httpMethod = "Post"
                "DeleteMapping" -> httpMethod = "Delete"
                "PutMapping" -> httpMethod = "Put"
                "PatchMapping" -> httpMethod = "Patch"
                else -> isHttpAnnotation = false
            }

            val hasSubUrlMapping = annotation.KeyValues.isNotEmpty()
            if (isHttpAnnotation && httpMethod.isNotEmpty() && hasSubUrlMapping) {
                val subUrl = annotation.KeyValues[0].Value
                val pureUrl = subUrl.removePrefix("\"").removeSuffix("\"")

                if (baseUrl.isNotEmpty()) {
                    route = "$baseUrl$pureUrl"
                } else {
                    route = pureUrl
                }
            }

            // todo: split by class
            // case 2
            if (annotation.Name == "RequestMapping") {
                val optUrl = annotation.KeyValues.filter { it.Key == "value" }
                val optMethod = annotation.KeyValues.filter { it.Key == "method" }
                if (optUrl.isNotEmpty() && optMethod.isNotEmpty()) {
                    when (optMethod[0].Value) {
                        "RequestMethod.GET", "GET" -> httpMethod = "Get"
                        "RequestMethod.POST", "POST" -> httpMethod = "Post"
                        "RequestMethod.DELETE", "DELETE" -> httpMethod = "Delete"
                        "RequestMethod.PUT", "PUT" -> httpMethod = "Put"
                        "RequestMethod.PATCH", "PATCH" -> httpMethod = "Patch"
                    }

                    val pureUrl = optUrl[0].Value.removePrefix("\"").removeSuffix("\"")
                    if (baseUrl.isNotEmpty()) {
                        route = "$baseUrl$pureUrl"
                    } else {
                        route = pureUrl
                    }
                }
            }
        }

        if (httpMethod.isNotEmpty()) {
            if (!route.startsWith("/")) {
                route = "/${route}"
            }

            route.replace("//", "/")

            resources = resources + ContainerSupply(
                sourceUrl = route,
                sourceHttpMethod = httpMethod,
                packageName = node.Package,
                className = node.NodeName,
                methodName = func.Name
            )
        }
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
}
