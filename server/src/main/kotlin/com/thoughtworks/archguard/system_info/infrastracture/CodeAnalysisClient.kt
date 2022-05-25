package com.thoughtworks.archguard.system_info.infrastracture

import com.thoughtworks.archguard.system_info.domain.AnalysisClientProxy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CodeAnalysisClient(@Value("\${client.host}") val baseUrl: String) : AnalysisClientProxy {

    private val log = LoggerFactory.getLogger(CodeAnalysisClient::class.java)

    override fun refreshThresholdCache() {
        try {
            RestTemplate().postForLocation("$baseUrl/api/bad-smell-threshold/reload", null, null)
            log.info("Auto refresh threshold cache in code-analysis")
        } catch (ex: Exception) {
            log.error("HTTP exception when trigger threshold auto refresh. {}", ex)
            throw ex
        }
    }
}
