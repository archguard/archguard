package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.cohesion.DataClassService
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/systems/{systemId}/cohesion")
class CohesionController(val shotgunSurgeryService: ShotgunSurgeryService, val dataClassService: DataClassService) {

    @PostMapping("/shotgun-surgery")
    fun getShotgunSurgeryWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ):
        ResponseEntity<ShotgunSurgeryListDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val result = shotgunSurgeryService.getShotgunSurgeryWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(
            ShotgunSurgeryListDto(
                result.second.map { ShotgunSurgeryDto(it.commitId, it.commitMessage, it.clazzes) },
                result.first, offset / limit + 1
            )
        )
    }

    @PostMapping("/data-class")
    fun getDataClassWithTotalCount(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ):
        ResponseEntity<DataClassDto> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        val result = dataClassService.getDataClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(
            DataClassDto(
                result.second,
                result.first, offset / limit + 1
            )
        )
    }
}

data class ShotgunSurgeryListDto(val data: List<ShotgunSurgeryDto>, val count: Long, val currentPageNumber: Long)

data class ShotgunSurgeryDto(val commitId: String, val commitMessage: String, val clazzes: List<ClassVO>)

data class DataClassDto(val data: List<DataClass>, val count: Long, val currentPageNumber: Long)
