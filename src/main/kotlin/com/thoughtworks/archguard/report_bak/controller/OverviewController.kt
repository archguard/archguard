package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.report_bak.domain.model.Overview
import com.thoughtworks.archguard.report_bak.domain.service.OverviewService
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