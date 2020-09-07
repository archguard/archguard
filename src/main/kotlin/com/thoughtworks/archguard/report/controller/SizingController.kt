package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.sizing.ClassSizingListWithLineDto
import com.thoughtworks.archguard.report.domain.sizing.ClassSizingListWithMethodCountDto
import com.thoughtworks.archguard.report.domain.sizing.MethodSizingListDto
import com.thoughtworks.archguard.report.domain.service.PackagesSizingListDto
import com.thoughtworks.archguard.report.domain.sizing.SizingService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/sizing")
class SizingController(val sizingService: SizingService) {
    @Value("\${threshold.method.line}")
    private val methodSizingThreshold: Int = 0

    @Value("\${threshold.class.line}")
    private val classSizingThreshold: Int = 0

    @Value("\${threshold.class.method.count}")
    private val classMethodCountSizingThreshold: Int = 0


    @Value("\${threshold.package.line}")
    private val packageSizingLineThreshold: Int = 0

    @Value("\${threshold.package.class.count}")
    private val packageClassCountSizingThreshold: Int = 0


    @GetMapping("/packages/above-line-threshold")
    fun getPackagesAboveLineThreshold(@PathVariable("systemId") systemId: Long,
                                      @RequestParam(value = "numberPerPage") limit: Long,
                                      @RequestParam(value = "currentPageNumber") currentPageNumber: Long)
            : ResponseEntity<PackagesSizingListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getPackageSizingListAboveLineThreshold(systemId, packageSizingLineThreshold, limit, offset))
    }

    @GetMapping("/packages/above-threshold")
    fun getPackagesAboveThreshold(@PathVariable("systemId") systemId: Long,
                                      @RequestParam(value = "numberPerPage") limit: Long,
                                      @RequestParam(value = "currentPageNumber") currentPageNumber: Long)
            : ResponseEntity<PackagesSizingListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getPackageClassCountSizingAboveThreshold(systemId, packageClassCountSizingThreshold, limit, offset))
    }

    @GetMapping("/methods/above-threshold")
    fun getMethodsAboveLineThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<MethodSizingListDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getMethodSizingListAboveLineThreshold(systemId, methodSizingThreshold, limit, offset))
    }

    @GetMapping("/classes/above-line-threshold")
    fun getClassesAboveLineThreshold(@PathVariable("systemId") systemId: Long,
                                     @RequestParam(value = "numberPerPage") limit: Long,
                                     @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<ClassSizingListWithLineDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getClassSizingListAboveLineThreshold(systemId, classSizingThreshold, limit, offset))
    }


    @GetMapping("/classes/above-method-count-threshold")
    fun getClassesAboveMethodCountThreshold(@PathVariable("systemId") systemId: Long,
                                            @RequestParam(value = "numberPerPage") limit: Long,
                                            @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<ClassSizingListWithMethodCountDto> {
        val offset = (currentPageNumber - 1) * limit
        return ResponseEntity.ok(sizingService.getClassSizingListAboveMethodCountThreshold(systemId, classMethodCountSizingThreshold, limit, offset))
    }


}

