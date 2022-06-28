package org.archguard.domain.comparison

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

