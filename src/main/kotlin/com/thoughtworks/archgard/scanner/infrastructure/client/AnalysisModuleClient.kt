package com.thoughtworks.archgard.scanner.infrastructure.client

import com.thoughtworks.archgard.scanner2.controller.MetricController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*


@Component
class AnalysisModuleClient(@Value("\${module.client.host}") val url: String,
                           val metricController: MetricController) {

    private val log = LoggerFactory.getLogger(AnalysisModuleClient::class.java)

    fun autoDefine(systemId: Long) {
        val params = HashMap<String, Any>()
        params["systemId"] = systemId
        RestTemplate().postForLocation("$url/systems/{systemId}/logic-modules/auto-define", null, params)
        log.info("send auto define request to module service")
    }

    fun metricsAnalysis(systemId: Long) {
        val params = HashMap<String, Any>()
        params["systemId"] = systemId
        metricController.persistClassMetrics(systemId)
        log.info("send metrics analysis request to module service")
    }

}
