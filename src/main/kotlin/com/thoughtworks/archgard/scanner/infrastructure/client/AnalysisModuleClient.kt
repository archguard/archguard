package com.thoughtworks.archgard.scanner.infrastructure.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AnalysisModuleClient (@Value("\${module.client.host}") val url: String) {
    private val log = LoggerFactory.getLogger(AnalysisModuleClient::class.java)

    fun autoDefine(projectId: Long) {
        RestTemplate().postForLocation("$url/logic-modules/{projectId}/auto-define", Long::class.java, projectId)
        log.info("send auto define request to module service")
    }

}
