package com.thoughtworks.archguard.insights

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ScaInsight(
    val systemId: Long?,
    val name: String? = "Default",
    val field: String,
    val comparison: String,
    val version: String,
)

@RestController
@RequestMapping("/api/insights")
class InsightController {
    // 1. query by expression with cron config
    // 2. count after DSL with kotlin scripting ?
    // 3. save by expression and to influx db
    @GetMapping("/sca")
    fun filterByType(
        @RequestBody insight: ScaInsight,
    ): List<String> {
        return listOf()
    }
}
