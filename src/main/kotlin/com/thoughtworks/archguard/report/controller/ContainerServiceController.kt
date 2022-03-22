package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.ServicesMapService
import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/container-service")
class ContainerServiceController(val service: ServicesMapService) {
    @GetMapping
    fun getServicesMap(@PathVariable("systemId") systemId: Long) : List<ContainerDemand> {
        return service.findBySystemId(systemId)
    }
}