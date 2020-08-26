package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/metric/dit")
class DitMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassDitMetric(@PathVariable("projectId") projectId: Long,
                          @RequestParam className: String,
                          @RequestParam moduleName: String): Int {
        return metricsService.getClassDit(projectId, JClassVO(className, moduleName))
    }
}
