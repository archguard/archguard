package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.Dashboard
import com.thoughtworks.archguard.report.application.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/systems/{systemId}")
class DashboardController(val dashboardService: DashboardService) {

    @GetMapping("/dashboard")
    fun getDashboard(@PathVariable("systemId") systemId: Long): ResponseEntity<List<Dashboard>> {
        return ResponseEntity.ok(dashboardService.getDashboard(systemId))
    }

    @PostMapping("/dashboard")
    fun saveReport(@PathVariable("systemId") systemId: Long) {
        dashboardService.saveReport(systemId)
    }
}
