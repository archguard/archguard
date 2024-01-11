package org.archguard.rule.common

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CasingTest {
    @Test
    fun `should return false for COBOL casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.`IS-COBOL`(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for flat casing`() {
        // given
        val input = "somevariablename"

        // when
        val result = Casing.isflat(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for flat casing`() {
        // given
        val input = "SomeVariableName"

        // when
        val result = Casing.isflat(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for Pascal casing`() {
        // given
        val input = "SomeVariableName"

        // when
        val result = Casing.IsPacal(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for Pascal casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.IsPacal(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for kebab casing`() {
        // given
        val input = "some-variable-name"

        // when
        val result = Casing.`is-kebab`(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for kebab casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.`is-kebab`(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for snake casing`() {
        // given
        val input = "some_variable_name"

        // when
        val result = Casing.is_nake(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for snake casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.is_nake(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for MACRO casing`() {
        // given
        val input = "SOME_MACRO_NAME"

        // when
        val result = Casing.IS_MACRO(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for MACRO casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.IS_MACRO(input)

        // then
        assertFalse(result)
    }

    @Test
    fun `should return true for camel casing`() {
        // given
        val input = "someVariableName"

        // when
        val result = Casing.isCamel(input)

        // then
        assertTrue(result)
    }

    @Test
    fun `should return false for camel casing`() {
        // given
        val input = "SomeVariableName"

        // when
        val result = Casing.isCamel(input)

        // then
        assertFalse(result)
    }
}

