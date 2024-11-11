package org.archguard.scanner.analyser.domain.tokenizer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CodeNamingTokenizerTest {
    @Test
    fun `should tokenize input string into parts based on patterns`() {
        // given
        val input = "CamelCasePascalCase123NumberStart_underscore"
        val expectedTokens = listOf("camel", "case", "pascal", "case", "123", "number", "start", "underscore")
        val tokenizer = CodeNamingTokenizer()

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(expectedTokens, tokens)
    }

    @Test
    fun `should tokenize and discard empty strings`() {
        // given
        val input = "camelCasePascalCase123Number_"
        val expectedTokens = listOf("camel", "case", "pascal", "case", "123", "number")
        val tokenizer = CodeNamingTokenizer(object : RegexTokenizerOptions {
            override val pattern: Regex? = null
            override val discardEmpty: Boolean = true
            override val gaps: Boolean? = null
        })

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(expectedTokens, tokens)
    }
}
