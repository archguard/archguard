package com.thoughtworks.archguard.architecture.controller

import com.thoughtworks.archguard.architecture.domain.service.ArchSystemService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/arch-systems")
class ArchSystemController(val archSystemService: ArchSystemService) {

    @PostMapping
    fun createArchSystem(@RequestBody request: ArchSystemCreateRequest): ArchSystemCreateResponse {
        val archSystem = archSystemService.create(request.name)
        return ArchSystemCreateResponse(archSystem.id, archSystem.name)
    }
}
