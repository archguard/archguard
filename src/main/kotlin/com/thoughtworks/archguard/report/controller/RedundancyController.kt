package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPairDTO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPairListDTO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationService
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/systems/{systemId}/redundancy")
class RedundancyController(
    val redundancyService: RedundancyService,
    val overGeneralizationService: OverGeneralizationService
) {

    @PostMapping("/class/one-method")
    fun getOneMethodClassWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ):
        ResponseEntity<OneMethodClassDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val result = redundancyService.getOneMethodClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneMethodClassDto(result.second, result.first, offset / limit + 1))
    }

    @PostMapping("/class/one-field")
    fun getOneFieldClassWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ):
        ResponseEntity<OneFieldDataClassDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val result = redundancyService.getOneFieldClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneFieldDataClassDto(result.second, result.first, offset / limit + 1))
    }

    @PostMapping("/class/over-generalization")
    fun getOverGeneralizationClassWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ):
        ResponseEntity<OverGeneralizationPairListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val result = overGeneralizationService.getOneExtendsWithTotalCount(systemId, limit, offset)
        val data = result.second.map { pair -> OverGeneralizationPairDTO.create(pair) }.toList()
        return ResponseEntity.ok(OverGeneralizationPairListDTO(data, result.first, offset / limit + 1))
    }
}

data class OneMethodClassDto(val data: List<ClassVO>, val count: Long, val currentPageNumber: Long)
data class OneFieldDataClassDto(val data: List<DataClass>, val count: Long, val currentPageNumber: Long)
