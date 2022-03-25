package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.ServicesMapService
import com.thoughtworks.archguard.report.domain.container.ContainerDemand
import com.thoughtworks.archguard.report.domain.container.ContainerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/container-service")
class ContainerServiceController(val service: ServicesMapService) {
    @GetMapping("{systemId}/")
    fun getServicesMap(@PathVariable("systemId") systemId: Long) : ContainerService {
        return service.findBySystemId(systemId)
    }
}