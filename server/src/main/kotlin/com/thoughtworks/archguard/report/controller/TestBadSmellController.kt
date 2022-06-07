package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.testing.IssueListDTO
import com.thoughtworks.archguard.report.domain.testing.MethodInfoListDTO
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/systems/{systemId}/test-bad-smell")
class TestBadSmellController(val testBadSmellService: TestBadSmellService) {

    @PostMapping("/static-methods")
    fun getModulesAboveLineThreshold(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<MethodInfoListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getStaticMethodList(systemId, limit, offset))
    }

    @PostMapping("/empty-test-methods")
    fun getEmptyTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getEmptyTestMethodList(systemId, limit, offset))
    }

    @PostMapping("/sleep-test-methods")
    fun getSleepTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {
        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getSleepTestMethodList(systemId, limit, offset))
    }

    @PostMapping("/ignore-test-methods")
    fun getIgnoreTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getIgnoreTestMethodList(systemId, limit, offset))
    }

    @PostMapping("/unassert-test-methods")
    fun getUnassertTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getUnassertTestMethodList(systemId, limit, offset))
    }

    @PostMapping("/multi-assert-test-methods")
    fun getMultiAssertTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getMultiAssertTestMethodList(systemId, limit, offset))
    }

    @PostMapping("/redundant-print-test-methods")
    fun getRedundantPrintTests(
        @PathVariable("systemId") systemId: Long,
        @RequestBody @Valid filterSizing: FilterSizingDto
    ): ResponseEntity<IssueListDTO> {

        val request = ValidPagingParam.validFilterParam(filterSizing)
        val limit = request.getLimit()
        val offset = request.getOffset()

        return ResponseEntity.ok(testBadSmellService.getRedundantPrintTestMethodList(systemId, limit, offset))
    }
}
