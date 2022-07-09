package org.archguard.domain.insight

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InsightsParserTest {

    @Test
    fun normalDSL() {
        val tokens = InsightsParser.tokenize("""dep_name = "5" and version = "1.3.7"""")
        assertEquals(7, tokens.size)

        listOf(
            Token(type = TokenType.Identifier, value = "dep_name", start = 0, end = 8),
            Token(type = TokenType.ComparisonKind, value = "=", start = 9, end = 10),
            Token(type = TokenType.StringKind, value = "\"5\"", start = 11, end = 14),
            Token(type = TokenType.Combinator, value = "and", start = 15, end = 18),
            Token(type = TokenType.Identifier, value = "version", start = 19, end = 26),
            Token(type = TokenType.ComparisonKind, value = "=", start = 27, end = 28),
            Token(type = TokenType.StringKind, value = "\"1.3.7\"", start = 29, end = 36)
        ).forEachIndexed { index, token ->
            assertEquals(token, tokens[index])
        }
    }

    @Test
    fun unpairedWrapper() {
        val tokens = InsightsParser.tokenize("dep_name = '\"`/%")

        assertEquals(7, tokens.size)
    }

    @Test
    fun stringValues() {
        val tokens = InsightsParser.tokenize("dep_name = 'log4j'")

        assertEquals(3, tokens.size)
        assertEquals(Token(type = TokenType.StringKind, value = "'log4j'", start = 11, end = 18), tokens[2])

        val tokens2 = InsightsParser.tokenize("dep_name = \"log4j\"")

        assertEquals(3, tokens2.size)
        assertEquals(Token(type = TokenType.StringKind, value = "\"log4j\"", start = 11, end = 18), tokens2[2])

        val tokens3 = InsightsParser.tokenize("dep_name = `log4j`")

        assertEquals(3, tokens3.size)
        assertEquals(Token(type = TokenType.StringKind, value = "`log4j`", start = 11, end = 18), tokens3[2])
    }

    @Test
    fun regexValue() {
        val tokens = InsightsParser.tokenize("dep_name = /log4j/")

        assertEquals(3, tokens.size)
        assertEquals(Token(type = TokenType.RegexKind, value = "/log4j/", start = 11, end = 18), tokens[2])
    }

    @Test
    fun likeValue() {
        val tokens = InsightsParser.tokenize("dep_name = %log4j%")

        assertEquals(3, tokens.size)
        assertEquals(Token(type = TokenType.LikeKind, value = "%log4j%", start = 11, end = 18), tokens[2])
    }

    @Test
    fun expression() {
        val sample = "dep_name = 'log4j'"
        val tokens = InsightsParser.tokenize(sample);

        assertEquals(3, tokens.size)
        assertEquals(Token(type = TokenType.ComparisonKind, value = "=", start = 9, end = 10), tokens[1])
    }

    @Test
    fun multipleExpr() {
        val tokens = InsightsParser.tokenize("""a = "1" and b = "2" && c = "3" or d = "4" || e = "5"""")

        assertEquals(19, tokens.size)

        assertEquals(Token(type = TokenType.Combinator, value = "and", start = 8, end = 11), tokens[3])
        assertEquals(Token(type = TokenType.Combinator, value = "&&", start = 20, end = 22), tokens[7])
        assertEquals(Token(type = TokenType.Combinator, value = "or", start = 31, end = 33), tokens[11])
        assertEquals(Token(type = TokenType.Combinator, value = "||", start = 42, end = 44), tokens[15])
    }

    @Test
    fun invalidToken() {
        val tokens = InsightsParser.tokenize("&")
        assertEquals(1, tokens.size)
        assertEquals(Token(TokenType.Unknown, value = "&", start = 0, end = 1), tokens[0])
    }

    @Test
    fun identifier() {
        val tokens = InsightsParser.tokenize("dep")
        assertEquals(1, tokens.size)
        assertEquals(Token(TokenType.Identifier, value = "dep", start = 0, end = 3), tokens[0])
    }
}