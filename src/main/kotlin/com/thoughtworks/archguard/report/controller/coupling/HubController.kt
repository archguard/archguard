package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.coupling.hub.HubService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/hub")
class HubController(val hubService: HubService) {

    @GetMapping("/classes/above-threshold")
    fun getClassesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn:  Boolean = false): ResponseEntity<ClassHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = hubService.getClassHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(ClassHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/methods/above-threshold")
    fun getMethodsAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn:  Boolean = false): ResponseEntity<MethodHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = hubService.getMethodHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(MethodHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/packages/above-threshold")
    fun getPackagesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                     @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean = false): ResponseEntity<PackageHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = hubService.getPackageHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(PackageHubListDto(resultList, count, offset / limit + 1, threshold))
    }

    @GetMapping("/modules/above-threshold")
    fun getModulesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn:  Boolean = false): ResponseEntity<ModuleHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        val (resultList, count, threshold) = hubService.getModuleHubListAboveThreshold(systemId, limit, offset, orderByFanIn)
        return ResponseEntity.ok(ModuleHubListDto(resultList, count, offset / limit + 1, threshold))
    }
}