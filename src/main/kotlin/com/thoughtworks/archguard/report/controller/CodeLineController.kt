package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.service.CodeLineService
import com.thoughtworks.archguard.report.domain.service.MethodLinesDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/codeline")
class CodeLineController(val codeLineService: CodeLineService) {
    @Value("\${threshold.method.line}")
    private val methodLineThreshold: Int = 0

    @GetMapping("/methods/above-threshold")
    fun getOverview(@PathVariable("systemId") systemId: Long,
                    @RequestParam(value = "numberPerPage") limit: Long,
                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<MethodLinesDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(codeLineService.getMethodLinesAboveThreshold(systemId, methodLineThreshold, limit, offset))
    }


}

