package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
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

    @PutMapping("/config")
    fun addConfigure(@RequestBody configs: List<ConfigureDTO>): List<String> {
        return configureService.addConfigure(configs)
    }

    @GetMapping("/config")
    fun getConfigures(): List<ScannerConfigure> {
        return configureService.getConfigures()
    }

    @PostMapping("/config")
    fun updateConfigure(@RequestBody configs: List<ConfigureDTO>): UpdateDTO {
        return configureService.updateConfigure(configs)
    }
}