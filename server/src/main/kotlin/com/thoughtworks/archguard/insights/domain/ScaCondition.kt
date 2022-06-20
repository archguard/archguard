package com.thoughtworks.archguard.insights.domain

data class ScaCondition(
    val type: String,
    val comparison: String,
    val version: String
)

enum class Comparison {
    Equal,
    NotEqual,
    GreaterThan,
    GreaterThanOrEqual,
    LessThan,
    LessThanOrEqual,
    NotSupport;

    companion object {
        fun fromString(symbol: String): Comparison {
            return when (symbol) {
                ">" -> GreaterThan
                ">=" -> GreaterThanOrEqual
                "==" -> Equal
                "!=" -> NotEqual
                "<=" -> LessThanOrEqual
                "<" -> LessThan
                else -> NotSupport
            }
        }
    }
}
