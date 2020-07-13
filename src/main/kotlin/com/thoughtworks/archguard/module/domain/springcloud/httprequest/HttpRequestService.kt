package com.thoughtworks.archguard.module.domain.springcloud.httprequest

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.archguard.module.domain.JAnnotationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMethod
import java.lang.annotation.ElementType

@Service
class HttpRequestService(val jAnnotationRepository: JAnnotationRepository) {
    private val log = LoggerFactory.getLogger(HttpRequestService::class.java)
    private val objectMapper = ObjectMapper()


    fun getHttpRequests(): List<HttpRequest> {
        val httpRequests = mutableListOf<HttpRequest>()
        httpRequests.addAll(analyzeRequestMapping("RequestMapping"))
        httpRequests.addAll(analyzeRequestMapping("GetMapping",  RequestMethod.GET.name))
        httpRequests.addAll(analyzeRequestMapping("PutMapping", RequestMethod.PUT.name))
        httpRequests.addAll(analyzeRequestMapping("PostMapping", RequestMethod.POST.name))
        httpRequests.addAll(analyzeRequestMapping("DeleteMapping", RequestMethod.DELETE.name))

        return httpRequests
    }

    private fun analyzeRequestMapping(annotationName: String, defaultMethod: String? = null): List<HttpRequest> {
        val annotations = jAnnotationRepository.getJAnnotationWithValueByName(annotationName).filter { it.targetType == ElementType.METHOD.name }
        return annotations.map { HttpRequest(it.targetId, HttpRequestArg(applyDefaultValues(it.values, defaultMethod))) }.toList()
    }

    private fun applyDefaultValues(values: Map<String, String>?, method: String?): Map<String, String> {
        val defaultValues = values.orEmpty()
        method?: return defaultValues
        return defaultValues.plus(mapOf("method" to  objectMapper.writeValueAsString(listOf(listOf("", method)))))
    }
}
