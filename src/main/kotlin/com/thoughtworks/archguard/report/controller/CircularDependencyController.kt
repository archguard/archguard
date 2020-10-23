package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyListDto
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.models.MethodVO
import com.thoughtworks.archguard.report.domain.models.ModuleVO
import com.thoughtworks.archguard.report.domain.models.PackageVO
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
                                                  @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<ModuleVO>> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getModuleCircularDependencyWithTotalCount(systemId, limit, offset))
    }

    @GetMapping("/package")
    fun getPackageCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                   @RequestParam(value = "numberPerPage") limit: Long,
                                                   @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<PackageVO>> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getPackageCircularDependencyWithTotalCount(systemId, limit, offset))
    }

    @GetMapping("/class")
    fun getClassCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                 @RequestParam(value = "numberPerPage") limit: Long,
                                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<ClassVO>> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getClassCircularDependencyWithTotalCount(systemId, limit, offset))
    }

    @GetMapping("/method")
    fun getMethodCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                  @RequestParam(value = "numberPerPage") limit: Long,
                                                  @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<MethodVO>> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(circularDependencyService.getMethodCircularDependencyWithTotalCount(systemId, limit, offset))
    }
}