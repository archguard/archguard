package com.thoughtworks.archguard.insights.domain

import kotlin.math.roundToInt

data class CustomInsight (
    val id: Long? = null,
    val systemId: Long = 0L,
    val name: String? = "Default",
    val expression: String,
    val type: String = "sca",
    val schedule: String? = "",
)

data class InsightData(val date: String, val name: String?, val value: Int, val type: String, val systemId: String) {
    fun toInflux(size: Int): String {
        return "insights,name=${this.name},system=${this.systemId},type=${this.type} value=$size"
    }

    companion object {
        fun fromCustomInsight(customInsight: CustomInsight): InsightData {
            return InsightData(
                date = "",
                name = customInsight.name,
                value = 0,
                type = customInsight.type,
                systemId = customInsight.systemId.toString()
            )
        }

        fun fromInflux(it: List<String>) = InsightData(
            date = it[0],
            name = it[1],
            systemId = it[2],
            type = it[3],
            value = it[4].toDouble().roundToInt()
        )
    }
}
