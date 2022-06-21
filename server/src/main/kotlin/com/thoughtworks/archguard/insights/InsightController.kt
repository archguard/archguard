package com.thoughtworks.archguard.insights

import org.archguard.domain.insight.InsightModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class InsightDto(
    val systemId: Long,
    val name: String? = "Default",
    val expression: String,
)

@RestController
@RequestMapping("/api/insights")
class InsightController(val insightService: InsightService) {
    // 1. query by expression with cron config
    // 2. count after DSL with kotlin scripting ?
    // 3. save by expression and to influx db
    @GetMapping("/sca")
    fun scaInsight(@RequestBody insight: InsightDto): Long {
        val insightModel = InsightModel.parse(insight.expression) ?: throw RuntimeException("invalid $insight")
        val count = insightService.byScaArtifact(insight.systemId, insightModel.field)
        return count
    }
}
