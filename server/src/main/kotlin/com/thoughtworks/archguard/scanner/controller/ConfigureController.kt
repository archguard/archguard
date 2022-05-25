package com.thoughtworks.archguard.scanner.controller

import com.thoughtworks.archguard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateMessageDTO
import com.thoughtworks.archguard.scanner.domain.config.service.ScannerConfigureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scanner")
class ConfigureController {

    @Autowired
    private lateinit var configureService: ScannerConfigureService

    @GetMapping("/config")
    fun getConfigures(): List<ConfigureDTO> {
        return configureService.getConfigures()
    }

    @PostMapping("/config")
    fun updateConfigure(@RequestBody configs: List<UpdateDTO>): UpdateMessageDTO {
        return configureService.updateConfigure(configs)
    }
}
