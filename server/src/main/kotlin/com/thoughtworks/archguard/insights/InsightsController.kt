package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.evolution.domain.BadSmellSuite
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/insights")
class InsightsController {
    // query by expression with cron config
    // count after DSL with kotlin scripting ?
    @GetMapping("/sca")
    fun filterByType(
        @PathVariable("systemId") systemId: Long,
        @RequestBody scaCondition: ScaCondition
    ): List<BadSmellSuite> {
        return listOf()
    }

    // save by expression and to influx db
}