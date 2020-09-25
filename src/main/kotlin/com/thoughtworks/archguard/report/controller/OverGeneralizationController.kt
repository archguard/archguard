package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.overgeneralization.OverGeneralizationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/over-generalization")
class OverGeneralizationController(val overGeneralizationService: OverGeneralizationService) {
    @GetMapping("/class/one-extends")
    fun getOneMethodClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                        @RequestParam(value = "numberPerPage") limit: Long,
                                        @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OneMethodClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = overGeneralizationService.getOneExtendsWithTotalCount(systemId, limit, offset)
        return ResponseEntity.ok(OneMethodClassDto(result.second, result.first, offset / limit + 1))
    }
}