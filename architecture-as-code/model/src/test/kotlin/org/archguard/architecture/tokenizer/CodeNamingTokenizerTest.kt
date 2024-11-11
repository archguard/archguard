package org.archguard.architecture.tokenizer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CodeNamingTokenizerTest {

    //region Test Cases

    @Test
    fun `should tokenize camelCase correctly`() {
        // given
        val tokenizer = CodeNamingTokenizer()
        val input = "camelCaseInput"

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(3, tokens.size)
        assertEquals("camel", tokens[0])
        assertEquals("case", tokens[1])
    }

    @Test
    fun `should tokenize PascalCase correctly`() {
        // given
        val tokenizer = CodeNamingTokenizer()
        val input = "PascalCaseInput"

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(3, tokens.size)
        assertEquals("pascal", tokens[0])
        assertEquals("case", tokens[1])
    }

    @Test
    fun `should tokenize mixed case correctly`() {
        // given
        val tokenizer = CodeNamingTokenizer()
        val input = "Mixed1Case2Input"

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(3, tokens.size)
        assertEquals("mixed", tokens[0])
        assertEquals("case", tokens[1])
        assertEquals("input", tokens[2])
    }

    @Test
    fun `should tokenize underscore correctly`() {
        // given
        val tokenizer = CodeNamingTokenizer()
        val input = "under_score_input"

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(3, tokens.size)
        assertEquals("under", tokens[0])
        assertEquals("score", tokens[1])
        assertEquals("input", tokens[2])
    }

    @Test
    fun `should not include empty strings in the result`() {
        // given
        val tokenizer = CodeNamingTokenizer()
        val input = "camel_Case_Input_"

        // when
        val tokens = tokenizer.tokenize(input)

        // then
        assertNotNull(tokens)
        assertEquals(3, tokens.size)
        assertEquals("camel", tokens[0])
        assertEquals("case", tokens[1])
        assertEquals("input", tokens[2])
    }
}
