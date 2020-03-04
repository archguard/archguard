package com.thoughtworks.archguard.archguardpackage.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HelloController {

    @RequestMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }
}