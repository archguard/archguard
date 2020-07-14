package com.thoughtworks.archguard.module.domain.springcloud.httprequest

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.common.JsonUtils
import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMethod
import java.lang.annotation.ElementType

@Service
class HttpRequestService(val jAnnotationRepository: JAnnotationRepository, val jClassRepository: JClassRepository) {
    private val log = LoggerFactory.getLogger(HttpRequestService::class.java)


    fun getHttpRequests(): List<HttpRequest> {

        val httpRequestMethods = analyzeRequestMethods()
        val httpRequestClasses = analyzeRequestClasses()

        margeHttpRequestClassToMethod(httpRequestMethods, httpRequestClasses)

        return httpRequestMethods
    }

    private fun analyzeRequestMethods(): List<HttpRequest>{
        val httpRequestMethods = mutableListOf<HttpRequest>()
        val requestMethodMap = mapOf("RequestMapping" to null, "GetMapping" to RequestMethod.GET.name, "PutMapping" to RequestMethod.PUT.name, "PostMapping" to RequestMethod.POST.name, "DeleteMapping" to RequestMethod.DELETE.name)
        for ((key, value) in requestMethodMap){
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

    private fun margeHttpRequestClassToMethod(httpRequestMethods: List<HttpRequest>, httpRequestClasses: List<HttpRequest>) {
        for (httpRequestClass in httpRequestClasses) {
            val methods = jClassRepository.getMethodsById(httpRequestClass.targetId)
            httpRequestMethods.filter { it.targetId in methods }.forEach { it.arg.path = margePath(httpRequestClass.arg.path, it.arg.path) }
        }
    }

    private fun margePath(basePaths: List<String>, paths: List<String>): List<String> {
        val finalPaths = mutableListOf<String>()
        for (basePath in basePaths) {
            for (path in paths) {
                finalPaths.add(basePath + path)
            }
        }
        return finalPaths
    }

}
