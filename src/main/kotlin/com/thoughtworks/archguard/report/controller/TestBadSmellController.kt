package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.infrastructure.TestBadSmellCountDBO
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestBadSmellController(@Autowired val testBadSmellRepo: TestBadSmellRepo) {

    @GetMapping("/reports/test-bad-smells")
    fun getBadSmellReport(): List<TestBadSmellCountDBO> {
        return testBadSmellRepo.getTestBadSmellCount()
    }

}