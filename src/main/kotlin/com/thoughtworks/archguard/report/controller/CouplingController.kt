package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import com.thoughtworks.archguard.report.domain.dataclumps.ClassDataClumpsListDto
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceListDto
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/coupling")
class CouplingController(val dataClumpsService: DataClumpsService,
                         val deepInheritanceService: DeepInheritanceService,
                         val classCouplingRepository: CouplingRepository) {

    @GetMapping("/data-clumps")
    fun getClassesDataClumpsWithTotalCount(@PathVariable("systemId") systemId: Long,
                                           @RequestParam(value = "numberPerPage") limit: Long,
                                           @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<ClassDataClumpsListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(dataClumpsService.getClassDataClumpsWithTotalCount(systemId, limit, offset))
    }

    @GetMapping("/deep-inheritance")
    fun getDeepInheritanceWithTotalCount(@PathVariable("systemId") systemId: Long,
                                         @RequestParam(value = "numberPerPage") limit: Long,
                                         @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<DeepInheritanceListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(deepInheritanceService.getDeepInheritanceWithTotalCount(systemId, limit, offset))
    }

    @GetMapping("/")
    fun getAllCouplingData(@PathVariable("systemId") systemId: Long): ResponseEntity<List<ClassCoupling>> {
        return ResponseEntity.ok(classCouplingRepository.getCoupling(systemId))
    }
}