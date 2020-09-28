package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.hub.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/hub")
class HubController(val hubService: HubService) {

    @GetMapping("/classes/above-threshold")
    fun getClassesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<ClassHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getClassHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
    }

    @GetMapping("/methods/above-threshold")
    fun getMethodsAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<MethodHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getMethodHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
    }

    @GetMapping("/packages/above-threshold")
    fun getPackagesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                     @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<PackageHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getPackageHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
    }

    @GetMapping("/modules/above-threshold")
    fun getModulesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<ModuleHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getModuleHubListAboveThreshold(systemId, limit, offset, orderByFanIn))
    }
}