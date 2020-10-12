package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPairDTO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPairListDTO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationService
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/redundancy")
class RedundancyController(val redundancyService: RedundancyService,
                           val overGeneralizationService: OverGeneralizationService) {

    @GetMapping("/class/one-method")
    fun getOneMethodClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                        @RequestParam(value = "numberPerPage") limit: Long,
                                        @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OneMethodClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = redundancyService.getOneMethodClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneMethodClassDto(result.second, result.first, offset / limit + 1))
    }

    @GetMapping("/class/one-field")
    fun getOneFieldClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                       @RequestParam(value = "numberPerPage") limit: Long,
                                       @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OneFieldDataClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = redundancyService.getOneFieldClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneFieldDataClassDto(result.second, result.first, offset / limit + 1))
    }

    @GetMapping("/class/over-generalization")
    fun getOverGeneralizationClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                                 @RequestParam(value = "numberPerPage") limit: Long,
                                                 @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OverGeneralizationPairListDTO> {
        val offset = (currentPageNumber - 1) * limit
        val result = overGeneralizationService.getOneExtendsWithTotalCount(systemId, limit, offset)
        val data = result.second.map { pair -> OverGeneralizationPairDTO.create(pair) }.toList()
        return ResponseEntity.ok(OverGeneralizationPairListDTO(data, result.first, offset / limit + 1))
    }

}

data class OneMethodClassDto(val data: List<ClassVO>, val count: Long, val currentPageNumber: Long)
data class OneFieldDataClassDto(val data: List<DataClass>, val count: Long, val currentPageNumber: Long)