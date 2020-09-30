package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.testing.StaticMethodListDto
import com.thoughtworks.archguard.report.domain.testing.TestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/test")
class TestController(val testService: TestService) {

    @GetMapping("/static/methods")
    fun getModulesAboveLineThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long)
            : ResponseEntity<StaticMethodListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(testService.getStaticMethodList(systemId, limit, offset))
    }

}