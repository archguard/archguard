package com.thoughtworks.archguard.insights.domain

import kotlin.math.roundToInt

data class CustomInsight (
    val id: Long? = null,
    val systemId: Long = 0L,
    val name: String? = "Default",
    val expression: String,
    val type: String = "sca",
    val schedule: String? = "",
) {
    fun toInflux(size: Int): String {
        return "insights,name=${this.name},system=${this.systemId},type=${this.type} value=$size"
    }
}

data class InsightData(val date: String, val name: String?, val value: Int, val type: String) {
    companion object {
        fun fromInflux(it: List<String>) = InsightData(
            date = it[0],
            name = it[1],
            type = it[3],
            value = it[4].toDouble().roundToInt()
        )
    }
}
