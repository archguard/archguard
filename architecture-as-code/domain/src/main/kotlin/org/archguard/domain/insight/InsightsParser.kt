package org.archguard.domain.insight

import org.archguard.domain.comparison.Comparison
import org.archguard.domain.insight.support.InsightIllegalException

enum class TokenType {
    Combinator, Identifier, StringKind, RegexKind, LikeKind, ComparisonKind, VersionKind, Unknown;

    companion object {
        fun fromChar(ch: Char): TokenType = when (ch) {
            '\'', '"', '`' -> StringKind
            '/' -> RegexKind
            '@' -> LikeKind
            else -> Unknown
        }
    }
}

enum class QueryMode {
    StrictMode, RegexMode, LikeMode, SemverMode, // >= 1.0.0
    Invalid;

    companion object {
        @Suppress("unused")
        fun fromTokenType(type: TokenType) {
            when (type) {
                TokenType.StringKind -> StrictMode
                TokenType.RegexKind -> RegexMode
                TokenType.LikeKind -> LikeMode
                TokenType.VersionKind -> SemverMode
                else -> Invalid
            }
        }
    }
}

enum class CombinatorType {
    And, Or, Then, NotSupported;

    companion object {
        fun fromString(str: String) = when (str) {
            "and", "&&" -> And
            "or", "||" -> Or
            "then" -> Then
            else -> NotSupported
        }
    }
}

val VERSION_REG = Regex("""^\d+(\.\d+){0,2}(-\w+)?$""")
val VERSION_REG_LOOSE = Regex("""^\d[\w.-]*$""")
val CHAR_REG = Regex("[a-zA-Z_]")
val COMPARATOR_REG = Regex("[<>=!]")
val LOGIC_REG = Regex("[&|]")
val NEW_LINE = Regex("[ \t\n\r]")

data class Token(val type: TokenType, val value: String, val start: Int, val end: Int)

class Either<A, B> private constructor(private val innerVal: Any, val isLeft: Boolean) {
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

    fun getLeftOrNull(): A? {
        if (isLeft) {
            @Suppress("UNCHECKED_CAST") return this.innerVal as A
        }

        return null
    }

    fun getRightOrNull(): B? {
        if (!isLeft) {
            @Suppress("UNCHECKED_CAST") return this.innerVal as B
        }

        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Either<*, *>) return false

        if (innerVal != other.innerVal) return false
        if (isLeft != other.isLeft) return false

        return true
    }

    override fun hashCode(): Int {
        var result = innerVal.hashCode()
        result = 31 * result + isLeft.hashCode()
        return result
    }
}

data class QueryExpression(val left: String, val right: String, val queryMode: QueryMode, val comparison: Comparison)
data class QueryCombinator(val value: String, val type: CombinatorType)

/**
 * @property field field that will be filtered
 * @property regex regex that used to filtering field's value
 * @property relation This is the relation to previous condition
 */
data class RegexQuery(val field: String, val regex: Regex, val relation: CombinatorType?)

/**
 * @property operator `>= | <= | == | > | < | !=`
 * @property version a valid semver version string
 * @property relation This is the relation to previous condition
 */
data class VersionQuery(val filed: String, val operator: String, val version: String, val relation: CombinatorType?)

class Query private constructor(
    val query: List<Either<QueryExpression, QueryCombinator>>,
    val postqueries: List<Either<RegexQuery, VersionQuery>>,
) {
    companion object {
        fun fromTokens(tokens: List<Token>): Query {
            val allQueries = parseQueries(tokens)
            val postqueries = mutableListOf<Either<RegexQuery, VersionQuery>>()

            val thenIndex = allQueries.indexOfFirst {
                it.getRightOrNull()?.type == CombinatorType.Then
            }
            val hasThen = thenIndex >= 0

            return if (!hasThen) {
                queryWithoutThen(allQueries, postqueries)
            } else {
                queryWithThen(allQueries, postqueries, thenIndex)
            }
        }

        private fun parseQueries(tokens: List<Token>): List<Either<QueryExpression, QueryCombinator>> {
            var left: String? = null
            var comparison: Comparison? = null
            var valid = false
            val result: MutableList<Either<QueryExpression, QueryCombinator>> = mutableListOf()
            tokens.forEach {
                when (it.type) {
                    TokenType.Combinator -> {
                        if (!valid) {
                            throw InsightIllegalException("Combinator should followed by a full expression")
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

                    TokenType.StringKind, TokenType.RegexKind, TokenType.LikeKind -> {
                        valid = true
                        handleWrappingValue(left, comparison, it, result)
                    }

                    TokenType.VersionKind -> {
                        valid = true
                        if (left == null || comparison == null) {
                            throw InsightIllegalException("Version query is not valid")
                        }
                        result.add(Either.Left(QueryExpression(left!!, it.value, QueryMode.SemverMode, comparison!!)))
                    }

                    TokenType.Unknown -> {
                        // unreachable if use InsightsParser#parse
                        throw InsightIllegalException("Input should not contains unknown type token")
                    }
                }
            }

            if (!result.last().isLeft) {
                throw InsightIllegalException("Combinator should not presents at the end of query")
            }
            return result
        }

        private fun handleWrappingValue(
            left: String?,
            comparison: Comparison?,
            it: Token,
            result: MutableList<Either<QueryExpression, QueryCombinator>>,
        ) {
            if (left == null) {
                throw InsightIllegalException("Identifier is not presents")
            } else if (comparison == null) {
                throw InsightIllegalException("Comparator is not presents")
            }

            val pair = when (it.type) {
                TokenType.StringKind -> {
                    val pureString = it.value.removeSurrounding("\"").removeSurrounding("'").removeSurrounding("`")

                    Pair(pureString, QueryMode.StrictMode)
                }

                TokenType.LikeKind -> Pair(it.value.removeSurrounding("@"), QueryMode.LikeMode)

                TokenType.RegexKind -> Pair(it.value.removeSurrounding("/"), QueryMode.RegexMode)

                else -> {
                    throw RuntimeException("unexpected else branch")
                }
            }

            val queryExpression = QueryExpression(left, pair.first, pair.second, comparison)
            result.add(Either.Left(queryExpression))
        }

        private fun queryWithThen(
            allQueries: List<Either<QueryExpression, QueryCombinator>>,
            postqueries: MutableList<Either<RegexQuery, VersionQuery>>,
            thenIndex: Int,
        ): Query {
            val resultReversed = allQueries.reversed()
            var needBreak = false
            for ((index, current) in resultReversed.withIndex()) {
                if (current.isLeft) {
                    val postquery = current.getLeftOrNull()!!
                    val next = if (index < resultReversed.size - 1) {
                        resultReversed[index + 1]
                    } else {
                        null
                    }

                    addToPostQueries(
                        postquery, postqueries, if (next?.getRightOrNull()?.type == CombinatorType.Then) {
                            // first one don't have relation to previous one
                            needBreak = true
                            null
                        } else {
                            next?.getRightOrNull()?.type
                        }
                    )

                    if (needBreak) break
                }
            }

            val query = allQueries.slice(0 until thenIndex)

            val queryHasRegex = query.any { it.getLeftOrNull()?.queryMode == QueryMode.RegexMode }
            if (queryHasRegex) {
                throw InsightIllegalException("SQL Queries should not contains RegexMode conditions")
            }

            return Query(query, postqueries)
        }

        private fun addToPostQueries(
            postquery: QueryExpression,
            postqueries: MutableList<Either<RegexQuery, VersionQuery>>,
            combinatorType: CombinatorType?
        ) {
            when (postquery.queryMode) {
                QueryMode.RegexMode -> {
                    val regexQuery = RegexQuery(postquery.left, Regex(postquery.right), combinatorType)
                    postqueries.add(Either.Left(regexQuery))
                }

                QueryMode.SemverMode -> {
                    val versionQuery = VersionQuery(postquery.left, postquery.comparison.toString(), postquery.right, combinatorType)
                    postqueries.add(Either.Right(versionQuery))
                }

                else -> {
                    throw InsightIllegalException("Illegal query mode inside postqueries")
                }
            }
        }

        private fun queryWithoutThen(
            allQueries: List<Either<QueryExpression, QueryCombinator>>,
            postQueries: MutableList<Either<RegexQuery, VersionQuery>>,
        ): Query {
            val isPostQueriesValid = allQueries.all { !it.isLeft || isValidPostqueryMode(it) }

            if (isPostQueriesValid) {
                for ((index, value) in allQueries.withIndex()) {
                    if (!value.isLeft) continue
                    val postquery = value.getLeftOrNull()!!
                    addToPostQueries(
                        postquery, postQueries, if (index == 0) {
                            // first one don't have relation to previous one
                            null
                        } else {
                            allQueries[index - 1].getRightOrNull()!!.type
                        }
                    )
                }

                return Query(emptyList(), postQueries)
            } else return if (allQueries.any { it.getLeftOrNull()?.queryMode == QueryMode.RegexMode }) {
                throw InsightIllegalException("SQL Queries should not contains RegexMode conditions")
            } else {
                Query(allQueries, postQueries)
            }
        }

        private fun isValidPostqueryMode(it: Either<QueryExpression, QueryCombinator>) =
            it.getLeftOrNull()?.queryMode == QueryMode.RegexMode || it.getLeftOrNull()?.queryMode == QueryMode.SemverMode
    }

    fun toSQL(prefix: String = "WHERE"): String {
        if (query.isEmpty()) {
            return "$prefix 1=1"
        }

        val sb = StringBuilder()
        query.forEach {
            if (it.isLeft) {
                val expr = it.getLeftOrNull()!!
                when (expr.queryMode) {
                    QueryMode.StrictMode -> {
                        sb.append("${expr.left} ${expr.comparison} '${expr.right.replace("'", "''")}'")
                    }

                    QueryMode.LikeMode -> {
                        if (expr.comparison == Comparison.Equal) {
                            sb.append("${expr.left} LIKE '${expr.right.replace("'", "''")}'")
                        } else {
                            sb.append("${expr.left} NOT LIKE '${expr.right.replace("'", "''")}'")
                        }
                    }

                    QueryMode.RegexMode -> {
                        throw InsightIllegalException("SQL query is not support regex mode")
                    }

                    QueryMode.SemverMode -> {
                        throw InsightIllegalException("SQL query is not support semver mode")
                    }

                    QueryMode.Invalid -> {
                        // do nothing
                    }
                }
            } else {
                val comb = it.getRightOrNull()!!
                when (comb.type) {
                    CombinatorType.And -> {
                        sb.append(" AND ")
                    }

                    CombinatorType.Or -> {
                        sb.append(" OR ")
                    }

                    CombinatorType.Then,
                    CombinatorType.NotSupported,
                    -> {
                        // do nothing
                    }
                }
            }
        }

        return "$prefix $sb"
    }
}

object InsightsParser {
    fun tokenize(text: String): List<Token> {
        val length = text.length
        var current = 0
        val tokens = mutableListOf<Token>()
        var maybeVersion = true

        while (current < length) {
            val c = text[current]
            when {
                VERSION_REG_LOOSE.matches(c.toString()) && maybeVersion -> {
                    var buff = c + ""
                    val pc = current
                    while (current < length - 1 && VERSION_REG_LOOSE.matches(buff)) {
                        buff += text[++current]
                    }

                    var finalVersion: String
                    if (current + 1 == length && buff.matches(VERSION_REG)) {
                        finalVersion = buff
                    } else if (buff.substring(0, buff.length - 1).matches(VERSION_REG)) {
                        finalVersion = buff.substring(0, buff.length - 1)
                    } else {
                        current = pc
                        maybeVersion = false
                        continue
                    }

                    tokens.add(Token(TokenType.VersionKind, finalVersion, pc, current++))
                }

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
                    maybeVersion = true
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

                    maybeVersion = true
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

                    maybeVersion = true
                }

                LOGIC_REG.matches(c.toString()) -> {
                    val start = current
                    if (current + 1 < length && text[current + 1] == c) {
                        current += 2
                        tokens.add(Token(type = TokenType.Combinator, value = "$c$c", start, end = current))
                    } else {
                        tokens.add(Token(type = TokenType.Unknown, value = c.toString(), start, end = ++current))
                    }

                    maybeVersion = true
                }

                NEW_LINE.matches(c.toString()) -> current++

                else -> {
                    tokens.add(Token(TokenType.Unknown, c.toString(), current, ++current))
                    maybeVersion = true
                }
            }
        }
        return tokens
    }

    private val queryCache = HashMap<String, Query>()
    fun parse(input: String): Query {
        if (queryCache.containsKey(input)) {
            return queryCache[input]!!
        }

        val tokens = tokenize(input)
        if (tokens.any { it.type == TokenType.Unknown }) {
            throw InsightIllegalException("Input is not a valid query")
        }

        val query = Query.fromTokens(tokens)
        queryCache[input] = query
        return query
    }
}