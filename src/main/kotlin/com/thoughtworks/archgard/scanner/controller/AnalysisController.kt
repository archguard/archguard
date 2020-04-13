package com.thoughtworks.archgard.scanner.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AnalysisController {

    @PostMapping("/dependency-analyses")
    fun analyseDependency(): String {
        return "Hello !"
    }

    @PostMapping("/sql-analyses")
    fun analyseSql(): String {
        return "Hello !"
    }
}