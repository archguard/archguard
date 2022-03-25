package org.archguard.scanner.sourcecode.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.sourcecode.frontend.ContainerResource
import org.archguard.scanner.sourcecode.frontend.ContainerService

class JavaApiAnalyser {
    var resources: List<ContainerResource> = listOf()

    fun analysisByNode(node: CodeDataStruct, _workspace: String) {
        val routeAnnotation = node.filterAnnotations("RestController", "RequestMapping")
        if (routeAnnotation.isNotEmpty()) {
            var baseUrl = ""
            val mappingAnnotation = node.filterAnnotations("RequestMapping")
            if (mappingAnnotation.isNotEmpty()) {
                baseUrl = mappingAnnotation[0].KeyValues[0].Value
            }
            node.Functions.forEach { createResource(it, baseUrl, node) }
        }
    }

    private fun createResource(func: CodeFunction, baseUrl: String, node: CodeDataStruct) {
        var httpMethod = "";
        var route = "";
        for (annotation in func.Annotations) {
            when (annotation.Name) {
                "GetMapping" -> httpMethod = "Get"
                "PostMapping" -> httpMethod = "Post"
                "DeleteMapping" -> httpMethod = "Delete"
                "PutMapping" -> httpMethod = "Put"
            }

            if(httpMethod.isNotEmpty()) {
                val subUrl = annotation.KeyValues[0].Value
                val pureUrl = subUrl.removePrefix("\"").removeSuffix("\"")

                if (baseUrl.isNotEmpty()) {
                    route = "$baseUrl/$pureUrl"
                } else {
                    route = pureUrl
                }
            }
        }

        if (route.isNotEmpty() && httpMethod.isNotEmpty()) {
            if (!route.startsWith("/")) {
                route = "/${route}"
            }

            resources = resources + ContainerResource(
                sourceUrl = route,
                sourceHttpMethod = httpMethod,
                packageName = node.Package,
                className = node.NodeName,
                methodName = func.Name
            )
        }
    }

    fun toContainerServices(): Array<ContainerService> {
        var componentCalls: Array<ContainerService> = arrayOf()

        val componentRef = ContainerService(name = "")
        componentRef.resources = this.resources

        componentCalls += componentRef
        return componentCalls
    }
}