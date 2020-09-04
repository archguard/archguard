package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.service.ClassSizingListDto
import com.thoughtworks.archguard.report.domain.service.MethodSizingListDto
import com.thoughtworks.archguard.report.domain.service.SizingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/codeline")
class SizingController(val sizingService: SizingService) {
    @Value("\${threshold.method.line}")
    private val methodSizingThreshold: Int = 0

    @Value("\${threshold.class.line}")
    private val classSizingThreshold: Int = 0

    @Value("\${threshold.class.method.count}")
    private val classMethodCountSizingThreshold: Int = 0

    @GetMapping("/methods/above-threshold")
    fun getMethodsAboveThreshold(@PathVariable("systemId") systemId: Long,
                                 @RequestParam(value = "numberPerPage") limit: Long,
                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<MethodSizingListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getMethodLinesAboveThreshold(systemId, methodSizingThreshold, limit, offset))
    }

    @GetMapping("/classes/above-threshold")
    fun getClassesAboveThreshold(@PathVariable("systemId") systemId: Long,
                                 @RequestParam(value = "numberPerPage") limit: Long,
                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<ClassSizingListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getClassLinesAboveThreshold(systemId, classSizingThreshold, limit, offset))
    }


}

