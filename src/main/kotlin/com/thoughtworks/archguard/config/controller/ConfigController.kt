package com.thoughtworks.archguard.config.controller

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.config.domain.NodeConfigure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/configures")
class ConfigController {
    @Autowired
    private lateinit var service: ConfigureService

    @GetMapping
    fun getConfigures() :List<NodeConfigure> {
        return service.getConfigures()
    }
}