package com.thoughtworks.archguard.scanner2.controller

import com.thoughtworks.archguard.scanner2.application.MetricPersistApplService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scanner/systems/{systemId}/metric")
class MetricController(val metricPersistService: MetricPersistApplService) {

    @PostMapping("/basic/persist")
    fun persistBasicMetrics(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistLevel2Metrics(systemId)
    }

    @PostMapping("/cycle-dependency/persist")
    fun persistCircularDependencyMetrics(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistCircularDependencyMetrics(systemId)
    }

    @PostMapping("/metric_dataclass/persist")
    fun persistDataClasses(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistDataClass(systemId)
    }
}
