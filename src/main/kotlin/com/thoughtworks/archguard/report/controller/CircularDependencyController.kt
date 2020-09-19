package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyListDto
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/circular-dependency")
class CircularDependencyController(val circularDependencyService: CircularDependencyService) {

    @GetMapping("/module")
    fun getModuleCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                  @RequestParam(value = "numberPerPage") limit: Long,
                                                  @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.MODULE))
    }

    @GetMapping("/package")
    fun getPackageCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                   @RequestParam(value = "numberPerPage") limit: Long,
                                                   @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.PACKAGE))
    }

    @GetMapping("/class")
    fun getClassCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                 @RequestParam(value = "numberPerPage") limit: Long,
                                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.CLASS))
    }

    @GetMapping("/method")
    fun getMethodCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                  @RequestParam(value = "numberPerPage") limit: Long,
                                                  @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.METHOD))
    }
}