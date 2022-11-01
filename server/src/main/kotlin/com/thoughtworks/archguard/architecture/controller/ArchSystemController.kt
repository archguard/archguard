package com.thoughtworks.archguard.architecture.controller

import com.thoughtworks.archguard.architecture.application.ArchSystemApplicationService
import com.thoughtworks.archguard.architecture.application.request.ArchSystemCreateRequest
import com.thoughtworks.archguard.architecture.application.response.ArchSystemCreateResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/arch-systems")
class ArchSystemController(val applicationService: ArchSystemApplicationService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createArchSystem(@RequestBody request: ArchSystemCreateRequest): ArchSystemCreateResponse {
        return applicationService.createArchSystem(request)
    }
}
