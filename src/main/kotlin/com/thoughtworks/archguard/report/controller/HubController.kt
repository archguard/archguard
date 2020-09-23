package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.hub.ClassHubListDto
import com.thoughtworks.archguard.report.domain.hub.HubService
import com.thoughtworks.archguard.report.domain.hub.MethodHubListDto
import com.thoughtworks.archguard.report.domain.hub.ModuleHubListDto
import com.thoughtworks.archguard.report.domain.hub.PackageHubListDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/hub")
class HubController(val hubService: HubService) {
    @Value("\${threshold.class.fanIn}")
    private val classFanInThreshold: Int = 0

    @Value("\${threshold.class.fanOut}")
    private val classFanOutThreshold: Int = 0

    @Value("\${threshold.method.fanIn}")
    private val methodFanInThreshold: Int = 0

    @Value("\${threshold.method.fanOut}")
    private val methodFanOutThreshold: Int = 0

    @Value("\${threshold.package.fanIn}")
    private val packageFanInThreshold: Int = 0

    @Value("\${threshold.package.fanOut}")
    private val packageFanOutThreshold: Int = 0

    @Value("\${threshold.module.fanIn}")
    private val moduleFanInThreshold: Int = 0

    @Value("\${threshold.module.fanOut}")
    private val moduleFanOutThreshold: Int = 0

    @GetMapping("/classes/above-threshold")
    fun getClassesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<ClassHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getClassHubListAboveThreshold(systemId, classFanInThreshold, classFanOutThreshold, limit, offset, orderByFanIn))
    }

    @GetMapping("/methods/above-threshold")
    fun getMethodsAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<MethodHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getMethodHubListAboveThreshold(systemId, methodFanInThreshold, methodFanOutThreshold, limit, offset, orderByFanIn))
    }

    @GetMapping("/packages/above-threshold")
    fun getPackagesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                     @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<PackageHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getPackageHubListAboveThreshold(systemId, packageFanInThreshold, packageFanOutThreshold, limit, offset, orderByFanIn))
    }

    @GetMapping("/modules/above-threshold")
    fun getModulesAboveHubThreshold(@PathVariable("systemId") systemId: Long,
                                    @RequestParam(value = "numberPerPage") limit: Long,
                                    @RequestParam(value = "currentPageNumber") currentPageNumber: Long,
                                    @RequestParam(value = "orderByFanIn") orderByFanIn: Boolean): ResponseEntity<ModuleHubListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(hubService.getModuleHubListAboveThreshold(systemId, moduleFanInThreshold, moduleFanOutThreshold, limit, offset, orderByFanIn))
    }
}