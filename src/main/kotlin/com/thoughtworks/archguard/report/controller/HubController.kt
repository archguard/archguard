package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassHubListDto
import com.thoughtworks.archguard.report.domain.coupling.hub.HubService
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodHubListDto
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleHubListDto
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageHubListDto
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