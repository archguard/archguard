package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.overview.OverviewDto
import com.thoughtworks.archguard.report.domain.overview.OverviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/systems/{systemId}/overview")
class OverviewController(val overviewService: OverviewService) {
    @GetMapping
    fun overview(@PathVariable("systemId") systemId: Long): ResponseEntity<OverviewDto> {
        return ResponseEntity.ok(overviewService.getOverview(systemId))
    }
}
