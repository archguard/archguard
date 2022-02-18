package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.report_bak.domain.model.Overview
import com.thoughtworks.archguard.report_bak.domain.service.BakOverviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BakOverviewController(@Autowired val overviewService: BakOverviewService) {

    @GetMapping("/reports/overview")
    fun getBadSmellReport(): Overview {
        return overviewService.getOverview()
    }

}