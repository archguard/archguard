package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.service.ConfigureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scanner")
class ConfigureController {

    @Autowired
    private lateinit var configureService: ConfigureService


    @GetMapping("/config")
    fun getConfigures(): List<ToolConfigure> {
        return configureService.getConfigures()
    }

    @PostMapping("/config")
    fun updateConfigure(@RequestBody configs: List<ConfigureDTO>): UpdateDTO {
        return configureService.updateConfigure(configs)
    }
}