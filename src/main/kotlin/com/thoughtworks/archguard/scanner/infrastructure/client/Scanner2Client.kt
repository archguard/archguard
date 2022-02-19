package com.thoughtworks.archguard.scanner.infrastructure.client

import com.thoughtworks.archguard.scanner2.controller.AccessController
import com.thoughtworks.archguard.scanner2.controller.MetricController
import com.thoughtworks.archguard.scanner2.controller.ShotgunSurgeryController
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*


@Component
class Scanner2Client(val metricController: MetricController,
                     val shotgunSurgeryController: ShotgunSurgeryController,
                     val accessController: AccessController) {

    private val log = LoggerFactory.getLogger(Scanner2Client::class.java)

    fun level2MetricsAnalysis(systemId: Long) {
        val params = HashMap<String, Any>()
        params["systemId"] = systemId
        metricController.persistBasicMetrics(systemId)
        metricController.persistCircularDependencyMetrics(systemId)
        metricController.persistDataClasses(systemId)
        shotgunSurgeryController.persist(systemId)
        accessController.persist(systemId)
        log.info("send metrics analysis request to module service")
    }

}
