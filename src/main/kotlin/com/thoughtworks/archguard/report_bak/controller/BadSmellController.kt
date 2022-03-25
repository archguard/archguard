package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.report_bak.infrastructure.BadSmellCountDBO
import com.thoughtworks.archguard.report_bak.infrastructure.BadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class BadSmellController(@Autowired val badSmellRepo: BadSmellRepo) {

    @GetMapping("/reports/bad-smells")
    fun getBadSmellReport(): List<BadSmellCountDBO> {
        return badSmellRepo.getBadSmellCount()
    }

}