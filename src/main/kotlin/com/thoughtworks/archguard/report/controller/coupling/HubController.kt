package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.coupling.hub.HubService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/systems/{systemId}/hub")
class HubController(val hubService: HubService) {

    @PostMapping("/classes/above-threshold")
    fun getClassesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestBody @Valid filterSizing: FilterSizingDto,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean = false): ResponseEntity<ClassHubListDto> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = hubService.getClassHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(ClassHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/methods/above-threshold")
    fun getMethodsAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestBody @Valid filterSizing: FilterSizingDto,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean = false): ResponseEntity<MethodHubListDto> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = hubService.getMethodHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(MethodHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/packages/above-threshold")
    fun getPackagesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestBody @Valid filterSizing: FilterSizingDto,
                                     @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean = false): ResponseEntity<PackageHubListDto> {


        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = hubService.getPackageHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(PackageHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/modules/above-threshold")
    fun getModulesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestBody @Valid filterSizing: FilterSizingDto,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean = false): ResponseEntity<ModuleHubListDto> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = hubService.getModuleHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(ModuleHubListDto(resultList, count, offset / limit + 1, threshold))
    }
}