package com.thoughtworks.archguard.code.module.domain.springcloud.httprequest

import com.thoughtworks.archguard.code.module.domain.JAnnotationRepository
import com.thoughtworks.archguard.code.module.domain.springcloud.SpringCloudServiceRepository
import org.archguard.json.JsonUtils
import org.archguard.protocol.http.HttpRequest
import org.archguard.protocol.http.HttpRequestArg
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMethod
import java.lang.annotation.ElementType

@Service
class HttpRequestService(val jAnnotationRepository: JAnnotationRepository, val springCloudServiceRepository: SpringCloudServiceRepository) {
    fun getHttpRequests(): List<HttpRequest> {

        val httpRequestMethods = analyzeRequestMethods()
        val httpRequestClasses = analyzeRequestClasses()

        mergeHttpRequestClassToMethod(httpRequestMethods, httpRequestClasses)

        return httpRequestMethods
    }

    private fun analyzeRequestMethods(): List<HttpRequest> {
        val httpRequestMethods = mutableListOf<HttpRequest>()
        val requestMethodMap = mapOf("RequestMapping" to null, "GetMapping" to RequestMethod.GET.name, "PutMapping" to RequestMethod.PUT.name, "PostMapping" to RequestMethod.POST.name, "DeleteMapping" to RequestMethod.DELETE.name)
        for ((key, value) in requestMethodMap) {
            httpRequestMethods.addAll(analyzeRequestMethod(key, value))
        }
        return httpRequestMethods
    }

    private fun analyzeRequestMethod(annotationName: String, defaultMethod: String? = null): List<HttpRequest> {
        val annotations = jAnnotationRepository.getJAnnotationWithValueByName(annotationName).filter { it.targetType == ElementType.METHOD.name }
        return annotations.map { HttpRequest(it.targetId, HttpRequestArg(applyDefaultValues(it.values, defaultMethod))) }.toList()
    }

    private fun applyDefaultValues(values: Map<String, String>?, method: String?): Map<String, String> {
        val defaultValues = values.orEmpty()
        method ?: return defaultValues
        return defaultValues.plus(mapOf("method" to JsonUtils.obj2json(listOf(listOf("", method)))))
    }

    private fun analyzeRequestClasses(): List<HttpRequest> {
        return jAnnotationRepository.getJAnnotationWithValueByName("RequestMapping").filter { it.targetType == ElementType.TYPE.name }.map { HttpRequest(it.targetId, HttpRequestArg(it.values.orEmpty())) }.toList()
    }

    private fun mergeHttpRequestClassToMethod(httpRequestMethods: List<HttpRequest>, httpRequestClasses: List<HttpRequest>) {
        for (httpRequestClass in httpRequestClasses) {
            val methods = springCloudServiceRepository.getMethodIdsByClassId(httpRequestClass.targetId)
            httpRequestMethods.filter { it.targetId in methods }.forEach {
                it.arg.paths = merge(httpRequestClass.arg.paths, it.arg.paths)
            }
        }
    }

    private fun merge(basePaths: List<String>, paths: List<String>): List<String> {
        val finalPaths = mutableListOf<String>()
        for (basePath in basePaths) {
            for (path in paths) {
                finalPaths.add(basePath + path)
            }
        }

        return finalPaths
    }
}
