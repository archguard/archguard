package com.thoughtworks.archgard.hub.controller

import com.thoughtworks.archgard.hub.domain.service.HubService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HubController {

    @Autowired
    private lateinit var hubService: HubService

    @GetMapping("/do")
    fun getModuleDependence() {
        return hubService.doScan()
    }
}