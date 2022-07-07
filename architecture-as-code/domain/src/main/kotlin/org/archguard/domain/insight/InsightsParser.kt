package org.archguard.domain.insight

enum class TokenType {
    Keyword, Operator, Identifier, Separator, StringKind, RegexKind, LikeKind, ComparisonKind, Unknown;

    companion object {
        fun fromChar(ch: Char): TokenType = when (ch) {
            '\'', '"', '`' -> StringKind
            '/' -> RegexKind
            '%' -> LikeKind
            else -> Unknown
        }
    }
}

enum class Comparison {
    Equal, NotEqual, GreaterThan, GreaterThanOrEqual, LessThan, LessThanOrEqual, NotSupported;

    companion object {
        fun fromString(str: String) = when (str) {
            "==" -> Equal
            "=" -> Equal
            "!=" -> NotEqual
            ">" -> GreaterThan
            ">=" -> GreaterThanOrEqual
            "<" -> LessThan
            "<=" -> LessThanOrEqual
            else -> NotSupported
        }
    }
}


// languages keywords
val INSIGHTS_KEYWORDS = listOf("field")

// todo: load from backend.
// sca
val SCA_KEYWORDS = listOf("dep_name", "dep_version")

// issues
val ISSUE_KEYWORDS = listOf("name", "rule_type", "severity")

// operatorish keywords
val OP_KEYWORDS = listOf("and", "or", "&&", "||")

val WRAPPER_SYMBOLS = listOf('\'', '"', '`', '/', '%')

val CHAR_REG = Regex("[a-zA-Z_]")
val COMPARATOR_REG = Regex("[<>=!]")

data class Token(val type: TokenType, val value: String, val start: Int, val end: Int)

class InsightsParser {
    fun tokenize(text: String): List<Token> {
        val length = text.length
        var current = 0
        val tokens = mutableListOf<Token>()

        while (current < length) {
            val c = text[current]
            when {
                CHAR_REG.matches(c.toString()) -> {
                    val start = current
                    var value = c.toString()
                    while (current + 1 < length && CHAR_REG.matches(text[current + 1].toString())) {
                        value += text[current + 1]
                        current++
                    }

                    current++

                    val token = when {
                        INSIGHTS_KEYWORDS.contains(value) -> Token(TokenType.Keyword, value, start, current)
                        OP_KEYWORDS.contains(value) -> Token(TokenType.Operator, value, start, current)
                        else -> Token(TokenType.Identifier, value, start, current)
                    }
                    tokens.add(token)
                }

                WRAPPER_SYMBOLS.contains(c) -> {
                    val endChar = c
                    var value = c.toString()
                    val start = current

                    while (current + 1 <= length && text[current + 1] != endChar) {
                        value += text[current + 1]
                        current += 1
                    }

                    if (value != c.toString()) {
                        current += 1
                        value += text[current]
                        tokens.add(Token(TokenType.fromChar(endChar), value, start, ++current))
                    } else {
                        current = start
                    }
                }

                COMPARATOR_REG.matches(c.toString()) -> {
                    val start = current
                    var value = c.toString()

                    while (current < length && COMPARATOR_REG.matches(text[current + 1].toString())) {
                        value += text[current + 1]
                        current += 1
                    }
                    current++

                    tokens.add(Token(TokenType.ComparisonKind, value, start, current))
                }

                c == ':' -> tokens.add(Token(TokenType.Separator, c.toString(), current, ++current))

                c == ' ' -> current++

                else -> {
                    tokens.add(Token(TokenType.Unknown, c.toString(), current, ++current))
                }
            }
        }

        return tokens
    }
}