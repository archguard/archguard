package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/metric/noc")
class NocMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassNocMetric(@PathVariable("projectId") projectId: Long,
                          @RequestParam className: String,
                          @RequestParam moduleName: String): Int {
        return metricsService.getClassNoc(projectId, JClassVO(className, moduleName))
    }
}
