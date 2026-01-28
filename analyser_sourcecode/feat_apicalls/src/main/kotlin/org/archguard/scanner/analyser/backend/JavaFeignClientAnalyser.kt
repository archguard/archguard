package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.context.ContainerDemand
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.scanner.analyser.base.ApiAnalyser

/**
 * Analyser for Spring Cloud Feign Client API calls.
 * 
 * Supports the following FeignClient annotation patterns:
 * - @FeignClient(name = "service-name")
 * - @FeignClient(value = "service-name")
 * - @FeignClient(name = "service-name", path = "/api")
 * - @FeignClient(url = "http://localhost:8080")
 * - org.springframework.cloud.openfeign.FeignClient
 * - org.springframework.cloud.netflix.feign.FeignClient (legacy)
 * - feign.FeignClient
 * 
 * Also supports HTTP method mappings within FeignClient interfaces:
 * - @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping
 * - @RequestMapping with method specification
 */
class JavaFeignClientAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()
    
    companion object {
        // All supported FeignClient annotation package prefixes
        private val FEIGN_CLIENT_PACKAGES = listOf(
            "org.springframework.cloud.openfeign.FeignClient",
            "org.springframework.cloud.netflix.feign.FeignClient",
            "feign.FeignClient"
        )
        
        // Supported HTTP method annotations
        private val HTTP_METHOD_ANNOTATIONS = mapOf(
            "GetMapping" to "GET",
            "PostMapping" to "POST",
            "PutMapping" to "PUT",
            "DeleteMapping" to "DELETE",
            "PatchMapping" to "PATCH",
            "RequestMapping" to null // needs to extract method from annotation values
        )
    }
    
    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        // Check if this is a FeignClient interface by checking imports and annotations
        if (!isFeignClientInterface(node)) {
            return
        }
        
        // Extract FeignClient annotation info
        val feignClientInfo = extractFeignClientInfo(node) ?: return
        
        // Analyze each method in the FeignClient interface
        node.Functions.forEach { func ->
            createDemand(func, feignClientInfo, node)
        }
    }
    
    /**
     * Check if the class has FeignClient annotation imported and used
     */
    private fun isFeignClientInterface(node: CodeDataStruct): Boolean {
        // Check if FeignClient is imported
        val hasFeignClientImport = node.Imports.any { import ->
            FEIGN_CLIENT_PACKAGES.any { pkg -> import.Source == pkg || import.Source.endsWith(".FeignClient") }
        }
        
        if (!hasFeignClientImport) {
            return false
        }
        
        // Check if class has FeignClient annotation
        return node.Annotations.any { annotation ->
            annotation.Name == "FeignClient"
        }
    }
    
    /**
     * Extract information from @FeignClient annotation
     */
    private fun extractFeignClientInfo(node: CodeDataStruct): FeignClientInfo? {
        val feignClientAnnotation = node.Annotations.find { it.Name == "FeignClient" } ?: return null
        
        var serviceName = ""
        var basePath = ""
        var baseUrl = ""
        
        feignClientAnnotation.KeyValues.forEach { kv ->
            when (kv.Key) {
                "name", "value", "" -> {
                    // Handle both named parameter and positional parameter
                    serviceName = extractStringValue(kv.Value)
                }
                "path" -> {
                    basePath = extractStringValue(kv.Value)
                }
                "url" -> {
                    baseUrl = extractStringValue(kv.Value)
                }
            }
        }
        
        // If service name is empty and baseUrl is not, use baseUrl as service identifier
        if (serviceName.isEmpty() && baseUrl.isNotEmpty()) {
            serviceName = baseUrl
        }
        
        return if (serviceName.isNotEmpty() || baseUrl.isNotEmpty()) {
            FeignClientInfo(serviceName, basePath, baseUrl)
        } else {
            null
        }
    }
    
    /**
     * Create a demand (API call) from a FeignClient method
     */
    private fun createDemand(func: CodeFunction, feignInfo: FeignClientInfo, node: CodeDataStruct) {
        var httpMethod = ""
        var path = ""
        
        for (annotation in func.Annotations) {
            when (annotation.Name) {
                "GetMapping" -> {
                    httpMethod = "GET"
                    path = extractPathFromAnnotation(annotation)
                }
                "PostMapping" -> {
                    httpMethod = "POST"
                    path = extractPathFromAnnotation(annotation)
                }
                "PutMapping" -> {
                    httpMethod = "PUT"
                    path = extractPathFromAnnotation(annotation)
                }
                "DeleteMapping" -> {
                    httpMethod = "DELETE"
                    path = extractPathFromAnnotation(annotation)
                }
                "PatchMapping" -> {
                    httpMethod = "PATCH"
                    path = extractPathFromAnnotation(annotation)
                }
                "RequestMapping" -> {
                    val (method, mappingPath) = extractFromRequestMapping(annotation)
                    httpMethod = method
                    path = mappingPath
                }
            }
            
            if (httpMethod.isNotEmpty()) {
                break
            }
        }
        
        if (httpMethod.isEmpty()) {
            return
        }
        
        // Construct the full target URL
        val fullPath = constructFullPath(feignInfo.basePath, path)
        val targetUrl = if (feignInfo.baseUrl.isNotEmpty()) {
            "${feignInfo.baseUrl}${fullPath}"
        } else {
            // Use service name as target indicator
            "${feignInfo.serviceName}:${fullPath}"
        }
        
        demands = demands + ContainerDemand(
            source_caller = "${node.Package}.${node.NodeName}.${func.Name}",
            call_routes = listOf(node.NodeName, func.Name),
            base = feignInfo.serviceName,
            target_url = targetUrl,
            target_http_method = httpMethod
        )
    }
    
    /**
     * Extract path from HTTP method annotation (GetMapping, PostMapping, etc.)
     */
    private fun extractPathFromAnnotation(annotation: chapi.domain.core.CodeAnnotation): String {
        if (annotation.KeyValues.isEmpty()) {
            return ""
        }
        
        // Path could be specified as 'value', 'path', or positional (empty key)
        val pathValue = annotation.KeyValues.find { 
            it.Key == "value" || it.Key == "path" || it.Key.isEmpty() 
        }
        
        return if (pathValue != null) {
            extractStringValue(pathValue.Value)
        } else {
            ""
        }
    }
    
    /**
     * Extract method and path from @RequestMapping annotation
     */
    private fun extractFromRequestMapping(annotation: chapi.domain.core.CodeAnnotation): Pair<String, String> {
        var method = "GET" // default to GET if not specified
        var path = ""
        
        annotation.KeyValues.forEach { kv ->
            when (kv.Key) {
                "value", "path", "" -> {
                    path = extractStringValue(kv.Value)
                }
                "method" -> {
                    method = extractHttpMethod(kv.Value)
                }
            }
        }
        
        return Pair(method, path)
    }
    
    /**
     * Extract HTTP method from RequestMethod enum value
     */
    private fun extractHttpMethod(value: String): String {
        val normalized = value.uppercase()
        return when {
            normalized.contains("GET") -> "GET"
            normalized.contains("POST") -> "POST"
            normalized.contains("PUT") -> "PUT"
            normalized.contains("DELETE") -> "DELETE"
            normalized.contains("PATCH") -> "PATCH"
            normalized.contains("HEAD") -> "HEAD"
            normalized.contains("OPTIONS") -> "OPTIONS"
            else -> "GET"
        }
    }
    
    /**
     * Extract string value by removing quotes and handling expressions
     */
    private fun extractStringValue(value: String): String {
        // Remove surrounding quotes
        var result = value.trim()
        if (result.startsWith("\"") && result.endsWith("\"")) {
            result = result.substring(1, result.length - 1)
        }
        // Handle single quotes (Kotlin string)
        if (result.startsWith("'") && result.endsWith("'")) {
            result = result.substring(1, result.length - 1)
        }
        return result
    }
    
    /**
     * Construct full path from base path and method path
     */
    private fun constructFullPath(basePath: String, methodPath: String): String {
        val normalizedBase = if (basePath.isNotEmpty() && !basePath.startsWith("/")) {
            "/$basePath"
        } else {
            basePath
        }
        
        val normalizedMethod = if (methodPath.isNotEmpty() && !methodPath.startsWith("/") && normalizedBase.isNotEmpty()) {
            "/$methodPath"
        } else if (methodPath.isNotEmpty() && !methodPath.startsWith("/")) {
            "/$methodPath"
        } else {
            methodPath
        }
        
        return "$normalizedBase$normalizedMethod".replace("//", "/")
    }
    
    override fun toContainerServices(): List<ContainerService> {
        return listOf(
            ContainerService(
                name = "",
                resources = resources,
                demands = demands
            )
        )
    }
    
    /**
     * Internal data class to hold FeignClient annotation info
     */
    private data class FeignClientInfo(
        val serviceName: String,
        val basePath: String,
        val baseUrl: String
    )
}
