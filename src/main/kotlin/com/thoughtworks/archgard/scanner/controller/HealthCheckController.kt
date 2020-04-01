package com.thoughtworks.archgard.scanner.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HealthCheckController {

    @GetMapping()
    fun addConfigure(): String {
        return "Hello !"
    }
}