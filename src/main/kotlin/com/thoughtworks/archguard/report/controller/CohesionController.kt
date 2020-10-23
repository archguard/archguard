package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.cohesion.DataClassService
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/cohesion")
class CohesionController(val shotgunSurgeryService: ShotgunSurgeryService, val dataClassService: DataClassService) {

    @GetMapping("/shotgun-surgery")
    fun getShotgunSurgeryWithTotalCount(@PathVariable("systemId") systemId: Long,
                                        @RequestParam(value = "numberPerPage") limit: Long,
                                        @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<ShotgunSurgeryListDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = shotgunSurgeryService.getShotgunSurgeryWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(ShotgunSurgeryListDto(
                result.second.map { ShotgunSurgeryDto(it.commitId, it.commitMessage, it.clazzes) },
                result.first, offset / limit + 1))
    }

    @GetMapping("/data-class")
    fun getDataClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                   @RequestParam(value = "numberPerPage") limit: Long,
                                   @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<DataClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = dataClassService.getDataClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(DataClassDto(
                result.second,
                result.first, offset / limit + 1))
    }

}

data class ShotgunSurgeryListDto(val data: List<ShotgunSurgeryDto>, val count: Long, val currentPageNumber: Long)

data class ShotgunSurgeryDto(val commitId: String, val commitMessage: String, val clazzes: List<ClassVO>)

data class DataClassDto(val data: List<DataClass>, val count: Long, val currentPageNumber: Long)