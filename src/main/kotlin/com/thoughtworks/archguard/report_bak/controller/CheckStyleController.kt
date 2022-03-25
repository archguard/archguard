package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.report_bak.domain.service.CheckStyleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/checkstyle")
class CheckStyleController {

    @Autowired
    lateinit var checkStyleService: CheckStyleService

    @GetMapping("/overview")
    fun getOverview() = checkStyleService.getOverview()
}