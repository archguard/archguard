package com.thoughtworks.archguard.report.controller.coupling

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
        val (resultList, count, threshold) = circularDependencyService.getModuleCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/package")
    fun getPackageCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                   @RequestParam(value = "numberPerPage") limit: Long,
                                                   @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<PackageVO>> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = circularDependencyService.getPackageCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/class")
    fun getClassCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                 @RequestParam(value = "numberPerPage") limit: Long,
                                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<ClassVO>> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = circularDependencyService.getClassCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/method")
    fun getMethodCircularDependencyWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                  @RequestParam(value = "numberPerPage") limit: Long,
                                                  @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<CircularDependencyListDto<MethodVO>> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = circularDependencyService.getMethodCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }
}