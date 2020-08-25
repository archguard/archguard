package com.thoughtworks.archguard.config.controller

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.config.domain.Configure
import org.springframework.beans.factory.annotation.Autowired
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
@RequestMapping("/projects/{projectId}/configures")
class ConfigController {
    @Autowired
    private lateinit var service: ConfigureService

    @GetMapping
    fun getConfigures(@PathVariable("projectId") projectId: Long): List<Configure> {
        return service.getConfigures(projectId)
    }

    @PostMapping
    fun create(@PathVariable("projectId") projectId: Long,
               @RequestBody config: Configure): ResponseEntity<Nothing> {
        service.create(config)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("projectId") projectId: Long,
               @PathVariable("id") id: String,
               @RequestBody config: Configure): ResponseEntity<Nothing> {
        service.update(id, config)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("projectId") projectId: Long,
               @PathVariable("id") id: String): ResponseEntity<Nothing> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/types/{type}")
    fun updateConfigsByType(@PathVariable("projectId") projectId: Long,
                            @PathVariable("type") type: String,
                            @RequestBody configs: List<Configure>): ResponseEntity<Nothing> {
        service.updateConfigsByType(projectId, type, configs)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
