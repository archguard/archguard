package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.appl.MetricsService
import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.metrics.domain.lcom4.LCOM4Service
import com.thoughtworks.archguard.module.domain.graph.Graph
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/metric/lcom4")
class LCOM4MetricController(val metricsService: MetricsService, val lcoM4Service: LCOM4Service) {

    @GetMapping("/class")
    fun getClassLCOM4Metric(@PathVariable("systemId") systemId: Long,
                            @RequestParam className: String,
                            @RequestParam moduleName: String): GraphWithConnectivityCount {
        val graphStore = metricsService.getClassLCOM4(systemId, JClassVO(className, moduleName))
        return GraphWithConnectivityCount(graphStore.getGraph(), graphStore.getConnectivityCount())
    }

    @GetMapping("/exceed")
    fun queryClassLCOM4ExceedThreshold(@PathVariable("systemId") systemId: Long,
                                       @RequestParam threshold: Int,
                                       @RequestParam limitPerPage: Int,
                                       @RequestParam numOfPage: Int): List<ClassLCOM4> {
        return lcoM4Service.getClassLCOM4ExceedThresholdWithPaging(systemId, threshold, limitPerPage, numOfPage)
    }
}

data class GraphWithConnectivityCount(val graph: Graph, val connectivityCount: Int)
