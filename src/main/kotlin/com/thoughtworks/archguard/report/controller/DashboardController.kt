package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.application.Dashboard
import com.thoughtworks.archguard.report.application.DashboardService
import com.thoughtworks.archguard.report.application.GraphData
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellLevel
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}")
class DashboardController(val dashboardService: DashboardService) {

    @GetMapping("/dashboard")
    fun getDashboard(@PathVariable("systemId") systemId: Long): ResponseEntity<List<Dashboard>> {
        return ResponseEntity.ok(dashboardService.getDashboard(systemId))
    }
}

class GroupData(eBadSmellType: BadSmellType, val level: BadSmellLevel, val graphData: List<GraphData>) {
    var type: String = eBadSmellType.value
}
