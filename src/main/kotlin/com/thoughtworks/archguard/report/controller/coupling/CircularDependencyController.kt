package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.models.MethodVO
import com.thoughtworks.archguard.report.domain.models.ModuleVO
import com.thoughtworks.archguard.report.domain.models.PackageVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/systems/{systemId}/circular-dependency")
class CircularDependencyController(val circularDependencyService: CircularDependencyService) {

    @PostMapping("/module")
    fun getModuleCircularDependencyWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<CircularDependencyListDto<ModuleVO>> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = circularDependencyService.getModuleCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/package")
    fun getPackageCircularDependencyWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<CircularDependencyListDto<PackageVO>> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = circularDependencyService.getPackageCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/class")
    fun getClassCircularDependencyWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<CircularDependencyListDto<ClassVO>> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = circularDependencyService.getClassCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/method")
    fun getMethodCircularDependencyWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<CircularDependencyListDto<MethodVO>> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = circularDependencyService.getMethodCircularDependencyWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(CircularDependencyListDto(resultList, count, offset / limit + 1, threshold))
    }
}
