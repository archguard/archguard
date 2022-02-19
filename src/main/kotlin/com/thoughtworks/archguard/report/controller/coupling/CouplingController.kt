package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.application.ClassCouplingAppService
import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceService
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/systems/{systemId}/coupling")
class CouplingController(val dataClumpsService: DataClumpsService,
                         val deepInheritanceService: DeepInheritanceService,
                         val classCouplingRepository: ClassCouplingRepository,
                         val classCouplingAppService: ClassCouplingAppService) {

    @PostMapping("/data-clumps")
    fun getClassesDataClumpsWithTotalCount(@PathVariable("systemId") systemId: Long, @RequestBody @Valid filterSizing: FilterSizingDto): ResponseEntity<ClassDataClumpsListDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = dataClumpsService.getClassDataClumpsWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(ClassDataClumpsListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/deep-inheritance")
    fun getDeepInheritanceWithTotalCount(@PathVariable("systemId") systemId: Long, @RequestBody @Valid filterSizing: FilterSizingDto): ResponseEntity<DeepInheritanceListDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val (resultList, count, threshold) = deepInheritanceService.getDeepInheritanceWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(DeepInheritanceListDto(resultList, count, offset / limit + 1, threshold))
    }

    @PostMapping("/class/")
    fun getAllClassCouplingData(@PathVariable("systemId") systemId: Long): ResponseEntity<List<ClassCoupling>> {
        return ResponseEntity.ok(classCouplingRepository.getAllCoupling(systemId))
    }

    @PostMapping("/class/quality-gate")
    fun getAllClassCouplingData(@PathVariable("systemId") systemId: Long, @RequestParam("qualityGateName") qualityGateName: String): ResponseEntity<List<ClassCoupling>> {
        return ResponseEntity.ok(classCouplingAppService.getCouplingFilterByQualityGate(systemId, qualityGateName))
    }
}