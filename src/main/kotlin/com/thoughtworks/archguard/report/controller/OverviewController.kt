package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.overview.BadSmellOverviewDto
import com.thoughtworks.archguard.report.domain.overview.OverviewService
import com.thoughtworks.archguard.report.domain.overview.SystemOverview
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/overview")
class OverviewController(val overviewService: OverviewService) {
    @GetMapping
    fun overview(@PathVariable("systemId") systemId: Long): ResponseEntity<BadSmellOverviewDto> {
        return ResponseEntity.ok(overviewService.getOverview(systemId))
    }

    @GetMapping("/system")
    fun systemOverview(@PathVariable("systemId") systemId: Long): ResponseEntity<SystemOverview> {
        return ResponseEntity.ok(overviewService.getSystemOverview(systemId))
    }
}
