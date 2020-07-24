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
@RequestMapping("/configures")
class ConfigController {
    @Autowired
    private lateinit var service: ConfigureService

    @GetMapping
    fun getConfigures(): List<Configure> {
        return service.getConfigures()
    }

    @PostMapping
    fun create(@RequestBody config: Configure): ResponseEntity<Nothing> {
        service.create(config)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody config: Configure): ResponseEntity<Nothing> {
        service.update(id, config)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Nothing> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping("/types/{type}")
    fun updateConfigsByType(@PathVariable("type") type: String,
                            @RequestBody configs: List<Configure>): ResponseEntity<Nothing> {
        service.updateConfigsByType(type, configs)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
