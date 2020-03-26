package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.model.Overview
import com.thoughtworks.archguard.report.domain.service.OverviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OverviewController(@Autowired val overviewService: OverviewService) {

    @GetMapping("/reports/overview")
    fun getBadSmellReport(): Overview {
        return overviewService.getOverview()
    }

}