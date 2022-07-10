package org.archguard.domain.insight

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

enum class TokenType {
    Combinator, Identifier, StringKind, RegexKind, LikeKind, ComparisonKind, Unknown;

    companion object {
        fun fromChar(ch: Char): TokenType = when (ch) {
            '\'', '"', '`' -> StringKind
            '/' -> RegexKind
            '%' -> LikeKind
            else -> Unknown
        }
    }
}

enum class QueryMode {
    StrictMode, RegexMode, LikeMode, Invalid;

    companion object {
        @Suppress("unused")
        fun fromTokenType(type: TokenType) {
            when (type) {
                TokenType.StringKind -> StrictMode
                TokenType.RegexKind -> RegexMode
                TokenType.LikeKind -> LikeMode
                else -> Invalid
            }
        }
    }
}

enum class CombinatorType {
    And, Or, NotSupported;

    companion object {
        fun fromString(str: String) = when (str) {
            "and", "&&" -> And
            "or", "||" -> Or
            else -> NotSupported
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

    override fun toString() = when (this) {
        Equal -> "="
        NotEqual -> "!="
        GreaterThan -> ">"
        GreaterThanOrEqual -> ">="
        LessThan -> "<"
        LessThanOrEqual -> "<="
        NotSupported -> "invalid"
    }
}

// operatorish keywords
val COMBINATOR_KEYWORDS = listOf("and", "or", "&&", "||")

val COMPARATOR_KEYWORDS = listOf("=", "==", ">", "<", ">=", "<=", "!=")

val WRAPPER_SYMBOLS = listOf('\'', '"', '`', '/', '%')

val CHAR_REG = Regex("[a-zA-Z_]")
val COMPARATOR_REG = Regex("[<>=!]")

data class Token(val type: TokenType, val value: String, val start: Int, val end: Int)

class Either<A, B> private constructor(private val innerVal: Any, val isA: Boolean) {
    companion object {
        @Suppress("FunctionName")
        fun <T : Any, O> Left(a: T): Either<T, O> {
            return Either(a, true)
        }

        @Suppress("FunctionName")
        fun <T : Any, O> Right(a: T): Either<O, T> {
            return Either(a, false)
        }
    }

    fun getAorNull(): A? {
        if (isA) {
            @Suppress("UNCHECKED_CAST") return this.innerVal as A
        }

        return null
    }

    fun getBorNull(): B? {
        if (!isA) {
            @Suppress("UNCHECKED_CAST") return this.innerVal as B
        }

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Either<*, *>) return false

        if (innerVal != other.innerVal) return false
        if (isA != other.isA) return false

        return true
    }

    override fun hashCode(): Int {
        var result = innerVal.hashCode()
        result = 31 * result + isA.hashCode()
        return result
    }
}

data class QueryExpression(val left: String, val right: String, val queryMode: QueryMode, val comparison: Comparison)
data class QueryCombinator(val value: String, val type: CombinatorType)

class Query private constructor(val data: List<Either<QueryExpression, QueryCombinator>>) {
    companion object {
        fun fromTokens(tokens: List<Token>): Query {
            var left: String? = null
            var comparison: Comparison? = null
            var valid = false
            val result: MutableList<Either<QueryExpression, QueryCombinator>> = mutableListOf()
            tokens.forEach {
                when (it.type) {
                    TokenType.Combinator -> {
                        if (!valid) {
                            throw IllegalArgumentException("Combinator should followed by a full expression")
                        }
                        left = null
                        comparison = null
                        valid = false
                        result.add(Either.Right(QueryCombinator(it.value, CombinatorType.fromString(it.value))))
                    }

                    TokenType.Identifier -> {
                        left = it.value
                    }

                    TokenType.ComparisonKind -> {
                        comparison = Comparison.fromString(it.value)
                    }

                    TokenType.StringKind -> {
                        if (left == null) {
                            throw IllegalArgumentException("Identifier is not presents")
                        } else if (comparison == null) {
                            throw IllegalArgumentException("Comparator is not presents")
                        } else {
                            result.add(
                                Either.Left(
                                    QueryExpression(
                                        left!!,
                                        it.value.removeSurrounding("\"").removeSurrounding("'"),
                                        QueryMode.StrictMode,
                                        comparison!!
                                    )
                                )
                            )
                            valid = true
                        }
                    }
                    TokenType.RegexKind -> {
                        if (left == null) {
                            throw IllegalArgumentException("Identifier is not presents")
                        } else if (comparison == null) {
                            throw IllegalArgumentException("Comparator is not presents")
                        } else {
                            result.add(
                                Either.Left(
                                    QueryExpression(
                                        left!!, it.value.removeSurrounding("/"), QueryMode.RegexMode, comparison!!
                                    )
                                )
                            )
                            valid = true
                        }
                    }
                    TokenType.LikeKind -> {
                        if (left == null) {
                            throw IllegalArgumentException("Identifier is not presents")
                        } else if (comparison == null) {
                            throw IllegalArgumentException("Comparator is not presents")
                        } else {
                            result.add(
                                Either.Left(
                                    QueryExpression(
                                        left!!, it.value.removeSurrounding("%"), QueryMode.LikeMode, comparison!!
                                    )
                                )
                            )
                            valid = true
                        }
                    }
                    TokenType.Unknown -> /* unreachable if use InsightsParser#parse */ throw IllegalArgumentException(
                        "Input should not contains unknown type token"
                    )
                }
            }

            if (!result.last().isA) {
                throw IllegalArgumentException("Combinator should not presents at the end of query")
            }

            return Query(result)
        }
    }

    override fun toString(): String {
        if (data.isEmpty()) {
            return ""
        }

        val sb = StringBuilder()
        data.forEach {
            if (it.isA) {
                val expr = it.getAorNull()!!
                when (expr.queryMode) {
                    QueryMode.StrictMode -> {
                        sb.append("${expr.left} ${expr.comparison} ${expr.right}")
                    }
                    QueryMode.LikeMode -> {
                        if (expr.comparison == Comparison.Equal) {
                            sb.append("${expr.left} like ${expr.right}")
                        } else {
                            sb.append("${expr.left} not like ${expr.right}")
                        }
                    }

                    QueryMode.RegexMode -> {
                        // TODO(CGQAQ): Remove this when Regex is supported
                        val result = sb.removeSuffix(" and ").removeSuffix(" or ")
                        sb.clear()
                        sb.append(result)
                    }

                    else -> { /* TODO(CGQAQ): Support Regex query */
                    }
                }
            } else {
                val comb = it.getBorNull()!!
                when (comb.type) {
                    CombinatorType.And -> {
                        sb.append(" and ")
                    }
                    CombinatorType.Or -> {
                        sb.append(" or ")
                    }
                    else -> { /* do nothing */
                    }
                }
            }
        }

        return "where $sb"
    }
}

class InsightsParser(val input: String) {
    companion object {
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
                            COMBINATOR_KEYWORDS.contains(value) -> Token(TokenType.Combinator, value, start, current)
                            else -> Token(TokenType.Identifier, value, start, current)
                        }
                        tokens.add(token)
                    }

                    WRAPPER_SYMBOLS.contains(c) -> {
                        var value = c.toString()
                        val start = current

                        while (current + 1 < length && text[current + 1] != c) {
                            value += text[current + 1]
                            current += 1
                        }


                        if (current + 1 < length && text[current + 1] == c) {
                            current++
                            value += c
                            tokens.add(Token(TokenType.fromChar(c), value, start, ++current))
                        } else {
                            current = start
                            tokens.add(Token(TokenType.Unknown, c.toString(), start, ++current))
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
                        tokens.add(
                            if (COMPARATOR_KEYWORDS.contains(value)) {
                                Token(TokenType.ComparisonKind, value, start, current)
                            } else {
                                Token(TokenType.Unknown, value, start, current)
                            }
                        )
                    }

                    c == '&' || c == '|' -> {
                        val start = current
                        if (current + 1 < length && text[current + 1] == c) {
                            current += 2
                            tokens.add(Token(type = TokenType.Combinator, value = "$c$c", start, end = current))
                        } else {
                            tokens.add(Token(type = TokenType.Unknown, value = c.toString(), start, end = ++current))
                        }
                    }

                    c == ' ' || c == '\t' -> current++

                    else -> {
                        tokens.add(Token(TokenType.Unknown, c.toString(), current, ++current))
                    }
                }
            }
            return tokens
        }
    }

    lateinit var query: Query

    fun parse(): Query {
        val tokens = tokenize(input)
        if (tokens.any { it.type == TokenType.Unknown }) {
            throw IllegalArgumentException("Input is not a valid query")
        }

        query = Query.fromTokens(tokens)
        return query
    }
}