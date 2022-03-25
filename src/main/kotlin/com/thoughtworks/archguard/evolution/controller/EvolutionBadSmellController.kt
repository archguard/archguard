package com.thoughtworks.archguard.evolution.controller

import com.thoughtworks.archguard.evolution.domain.BadSmellSuite
import com.thoughtworks.archguard.evolution.domain.BadSmellSuiteWithSelected
import com.thoughtworks.archguard.evolution.domain.BadSmellThresholdService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evolution")
class EvolutionBadSmellController(val badSmellThresholdService: BadSmellThresholdService) {

    @GetMapping("/badsmell-thresholds")
    fun getAllThresholds(): List<BadSmellSuite> {
        return badSmellThresholdService.getAllSuits()
    }

    @GetMapping("/badsmell-thresholds/system/{systemId}")
    fun getThresholdsBySystemId(@PathVariable("systemId") systemId: Long): List<BadSmellSuiteWithSelected> {
        return badSmellThresholdService.getBadSmellSuiteWithSelectedInfoBySystemId(systemId)
    }
}




