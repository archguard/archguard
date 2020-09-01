package com.thoughtworks.archguard.qualitygate.controller

import com.thoughtworks.archguard.qualitygate.domain.ProfileService
import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfileDTO
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
class ProfileController(val service: ProfileService) {

    @GetMapping("/{name}")
    fun getByName(@PathVariable("name") name: String): ResponseEntity<QualityGateProfileDTO> {
        return ResponseEntity.ok(service.getByNameOrDefault(name).toDto())
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<QualityGateProfileDTO>> {
        return ResponseEntity.ok(service.getAll().map { it.toDto() })
    }

    @PostMapping
    fun create(@RequestBody profile: QualityGateProfileDTO): ResponseEntity<Nothing> {
        service.create(profile.toProfile())
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: Long,
               @RequestBody profile: QualityGateProfileDTO): ResponseEntity<Nothing> {
        service.update(id, profile.toProfile())
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Nothing> {
        service.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
