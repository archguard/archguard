package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bad-smell-threshold")
class BadSmellThresholdController(val thresholdSuiteService: ThresholdSuiteService) {

    @PostMapping("/reload")
    fun reloadThresholdCache() {
        thresholdSuiteService.reloadAllSuites()
    }

}

