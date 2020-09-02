package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.model.MethodLine
import com.thoughtworks.archguard.report.domain.service.CodeLineService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/codeline")
class CodeLineController(val codeLineService: CodeLineService) {
    @Value("\${method.line.threshold}")
    private val methodLineThreshold: Int = 0

    @GetMapping("/methods/above-threshold")
    fun getOverview(@PathVariable("systemId") systemId: Long): ResponseEntity<List<MethodLine>> {
        return ResponseEntity.ok(codeLineService.getMethodLinesAboveThreshold(systemId, methodLineThreshold))
    }
}

