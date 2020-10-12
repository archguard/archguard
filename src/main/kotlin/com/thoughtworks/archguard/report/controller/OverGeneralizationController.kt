package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/over-generalization")
class OverGeneralizationController(val overGeneralizationService: OverGeneralizationService) {
    @GetMapping("/class/one-extends")
    fun getOneMethodClassWithTotalCount(@PathVariable("systemId") systemId: Long,
                                        @RequestParam(value = "numberPerPage") limit: Long,
                                        @RequestParam(value = "currentPageNumber") currentPageNumber: Long):
            ResponseEntity<OneMethodClassDto> {
        val offset = (currentPageNumber - 1) * limit
        val result = overGeneralizationService.getOneExtendsWithTotalCountOld(systemId, limit, offset)
        return ResponseEntity.ok(OneMethodClassDto(result.second, result.first, offset / limit + 1))
    }
}