package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.model.ClassVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/cohesion")
class CohesionController(val shotgunSurgeryService: ShotgunSurgeryService) {

    @GetMapping("/shotgun-surgery")
    fun getShotgunSurgeryithTotalCount(@PathVariable("systemId") systemId: Long,
                                       @RequestParam(value = "numberPerPage") limit: Long,
                                       @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<ShotgunSurgeryListDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = shotgunSurgeryService.getShotgunSurgeryWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(ShotgunSurgeryListDto(
                result.second.map { ShotgunSurgeryDto(it.commitId, it.commitMessage, it.clazzes) },
                result.first, offset / limit + 1))
    }
}

data class ShotgunSurgeryListDto(val data: List<ShotgunSurgeryDto>, val count: Long, val currentPageNumber: Long)

data class ShotgunSurgeryDto(val commitId: String, val commitMessage: String, val clazzes: List<ClassVO>)
