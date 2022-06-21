package com.thoughtworks.archguard.insights.domain

import com.thoughtworks.archguard.insights.domain.Comparison.*

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

class VersionComparison(val version: VersionNumber) {
    // usage: isFit(">=", "1.2.2")
    fun isFit(symbol: String, other: String): Boolean? {
        val comparison = Comparison.fromString(symbol)
        if (comparison == NotSupport) {
            return null
        }

        val otherVersion = VersionNumber.parse(other) ?: return null

        val result = version.compareTo(otherVersion)

        when {
            result == 0 && comparison == Equal -> {
                return true
            }

            result > 0 && comparison == GreaterThan -> {
                return true
            }

            result < 0 && comparison == LessThan -> {
                return true
            }

            result >= 0 && comparison == GreaterThanOrEqual -> {
                return true
            }

            result <= 0 && comparison == LessThanOrEqual -> {
                return true
            }

            else -> return false
        }

    }
}