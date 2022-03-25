package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.evaluation_bak.domain.TestBadSmellCount
import com.thoughtworks.archguard.report_bak.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BakTestBadSmellController(@Autowired val testBadSmellRepo: TestBadSmellRepo) {

    @GetMapping("/reports/test-bad-smells")
    fun getBadSmellReport(): List<TestBadSmellCount> {
        return testBadSmellRepo.getTestBadSmellCount()
    }

}