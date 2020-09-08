package com.thoughtworks.archgard.scanner2.controller


import com.thoughtworks.archgard.scanner2.appl.MetricPersistApplService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/metric")
class MetricController(val metricPersistService: MetricPersistApplService) {

    @PostMapping("/class/persist")
    fun persistClassMetrics(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistLevel2Metrics(systemId)
    }
}
