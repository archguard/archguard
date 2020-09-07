package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.hub.ClassHubListDto
import com.thoughtworks.archguard.report.domain.hub.HubService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/hub")
class HubController(val hubService: HubService) {
    @Value("\${threshold.class.fanIn}")
    private val classFanInThreshold: Int = 0

    @Value("\${threshold.class.fanOut}")
    private val classFanOutThreshold: Int = 0

    @GetMapping("/classes/above-threshold")
    fun getClassesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<ClassHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getClassHubListAboveThreshold(systemId, classFanInThreshold, classFanOutThreshold, limit, offset, orderByFanIn))
    }
}