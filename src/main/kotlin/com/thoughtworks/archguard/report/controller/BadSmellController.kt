package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.infrastructure.BadSmellCountDBO
import com.thoughtworks.archguard.report.infrastructure.BadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BadSmellController(@Autowired val badSmellRepo: BadSmellRepo) {

    @GetMapping("/reports/bad-smells")
    fun getBadSmellReport(): List<BadSmellCountDBO> {
        return badSmellRepo.getBadSmellCount()
    }

}