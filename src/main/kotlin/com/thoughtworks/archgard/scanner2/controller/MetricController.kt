package com.thoughtworks.archgard.scanner2.controller


import com.thoughtworks.archgard.scanner2.appl.MetricPersistApplService
import com.thoughtworks.archgard.scanner2.domain.service.circular.CircularService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/metric")
class MetricController(val metricPersistService: MetricPersistApplService,
                       val circularService: CircularService) {

    @PostMapping("/basic/persist")
    fun persistBasicMetrics(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistLevel2Metrics(systemId)
    }

    @PostMapping("/cycle-dependency/persist")
    fun persistCircularDependencyMetrics(@PathVariable("systemId") systemId: Long) {
        return circularService.persistCircularDependencyMetrics(systemId)
    }

    @PostMapping("/data_class/persist")
    fun persistDataClasses(@PathVariable("systemId") systemId: Long) {
        return metricPersistService.persistDataClass(systemId)
    }
}
