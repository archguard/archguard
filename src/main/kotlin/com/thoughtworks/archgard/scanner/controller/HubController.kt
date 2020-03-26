package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.hubexecutor.HubService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HubController {

    @Autowired
    private lateinit var hubService: HubService

    @PostMapping("/reports")
    fun scanModule(): ModuleScanResponse {
        return ModuleScanResponse(hubService.doScan())
    }

    data class ModuleScanResponse(val isRunning: Boolean)
}