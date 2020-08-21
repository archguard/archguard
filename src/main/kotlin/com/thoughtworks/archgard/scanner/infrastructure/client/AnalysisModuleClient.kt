package com.thoughtworks.archgard.scanner.infrastructure.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.HashMap



@Component
class AnalysisModuleClient (@Value("\${module.client.host}") val url: String) {
    private val log = LoggerFactory.getLogger(AnalysisModuleClient::class.java)

    fun autoDefine(projectId: Long) {
        val params = HashMap<String, Any>()
        params["projectId"] = projectId
        RestTemplate().postForLocation("$url/logic-modules/{projectId}/auto-define", null, params)
        log.info("send auto define request to module service")
    }

}
