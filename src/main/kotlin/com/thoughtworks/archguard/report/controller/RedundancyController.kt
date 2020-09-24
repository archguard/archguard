package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/redundancy")
class RedundancyController(val redundancyService: RedundancyService) {

    @GetMapping("/class/one-method")
    fun getOneMethodClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                        @RequestParam(value = "numberPerPage") limit: Long,
                                        @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OneMethodClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = redundancyService.getOneMethodClassWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneMethodClassDto(result.second, result.first, offset / limit + 1))
    }
}

data class OneMethodClassDto(val data: List<ClassVO>, val count: Long, val currentPageNumber: Long)