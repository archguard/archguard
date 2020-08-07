package com.thoughtworks.archguard.qualitygate.controller

import com.thoughtworks.archguard.config.domain.Configure
import com.thoughtworks.archguard.qualitygate.domain.ProfileService
import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfile
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
@RequestMapping("/quality-gate-profile")
class ProfileController {
    @Autowired
    private lateinit var service: ProfileService

    @GetMapping
    fun getAll() :ResponseEntity<List<QualityGateProfile>> {
        return ResponseEntity.ok(service.getAll())
    }

    @PostMapping
    fun create(@RequestBody profile: QualityGateProfile): ResponseEntity<Nothing> {
        service.create(profile)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: String,
               @RequestBody profile: QualityGateProfile): ResponseEntity<Nothing> {
        service.update(id, profile)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Nothing> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
