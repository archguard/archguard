package org.archguard.domain.insight

const val DOUBLE_QUOTE = "\""
const val SINGLE_QUOTE = "'"
const val PERCENT = "%"
const val SLASH = "/"
const val PERCENT_CHAR = '%'

// operatorish keywords
val COMBINATOR_KEYWORDS = listOf("AND", "and", "OR", "or", "&&", "||", "THEN", "then")
val COMPARATOR_KEYWORDS = listOf("=", "==", ">", "<", ">=", "<=", "!=")
val WRAPPER_SYMBOLS = listOf('\'', '"', '`', '/', '@')