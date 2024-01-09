package com.thoughtworks.archguard.config.controller

import org.archguard.config.Configure
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/configures")
class ConfigController(val configureService: ConfigureService) {

    @GetMapping
    fun getConfigures(@PathVariable("systemId") systemId: Long): List<Configure> {
        return configureService.getConfigures(systemId)
    }

    @PostMapping
    fun create(
        @PathVariable("systemId") systemId: Long,
        @RequestBody config: Configure
    ): ResponseEntity<Nothing> {
        configureService.create(config)
        config.systemId = systemId
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("systemId") systemId: Long,
        @PathVariable("id") id: String,
        @RequestBody config: Configure
    ): ResponseEntity<Nothing> {
        configureService.update(id, config)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("systemId") systemId: Long,
        @PathVariable("id") id: String
    ): ResponseEntity<Nothing> {
        configureService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/types/{type}")
    fun updateConfigsByType(
        @PathVariable("systemId") systemId: Long,
        @PathVariable("type") type: String,
        @RequestBody configs: List<Configure>
    ): ResponseEntity<Nothing> {
        configureService.updateConfigsByType(systemId, type, configs)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
