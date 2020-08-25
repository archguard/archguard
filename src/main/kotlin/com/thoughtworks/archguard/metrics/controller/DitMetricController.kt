package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/metric/dit")
class DitMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassDitMetric(@RequestParam className: String, @RequestParam moduleName: String): Int {
        return metricsService.getClassDit(JClassVO(className, moduleName))
    }
}