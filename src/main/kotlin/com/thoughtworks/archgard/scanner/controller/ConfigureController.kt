package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.service.ConfigureService
import com.thoughtworks.archgard.scanner.domain.config.dto.AddDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.DeleteDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scanner")
class ConfigureController {

    @Autowired
    private lateinit var configureService: ConfigureService

    @PutMapping("/config")
    fun addConfigure(@RequestBody config: ConfigureDTO): AddDTO {
        return configureService.addConfigure(config)
    }

    @GetMapping("/config")
    fun getConfigures(): List<ScannerConfigure> {
        return configureService.getConfigures()
    }

    @DeleteMapping("/config")
    fun deleteConfigure(@RequestBody config: ConfigureDTO): DeleteDTO {
        return configureService.deleteConfigure(config.id)
    }

    @PostMapping("/config")
    fun updateConfigure(@RequestBody config: ConfigureDTO): UpdateDTO {
        return configureService.updateConfigure(config)
    }
}