package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto
import org.springframework.web.bind.annotation.PostMapping
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
    @PostMapping("/sca")
    fun scaInsight(@RequestBody insight: InsightDto): List<ScaModelDto> {
        return insightService.byScaArtifact(insight.systemId, insight.expression)
    }
}
