package com.thoughtworks.archgard.scanner.infrastructure.client

import com.thoughtworks.archgard.scanner2.controller.MetricController
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*


@Component
class Scanner2Client(val metricController: MetricController) {

    private val log = LoggerFactory.getLogger(Scanner2Client::class.java)

    fun level2MetricsAnalysis(systemId: Long) {
        val params = HashMap<String, Any>()
        params["systemId"] = systemId
        metricController.persistBasicMetrics(systemId)
        metricController.persistCircularDependencyMetrics(systemId)
        log.info("send metrics analysis request to module service")
    }

}
