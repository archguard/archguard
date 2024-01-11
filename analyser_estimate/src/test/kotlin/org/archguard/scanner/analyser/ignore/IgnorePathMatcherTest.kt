package org.archguard.scanner.analyser.ignore;

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SimpleMatcherIgnoreTest {

    @Test
    fun should_return_true_when_path_matches() {
        // Given
        val path = "example/path"
        val matcher = SimpleMatcherIgnore(path)

        // When
        val result = matcher.match(path)

        // Then
        assertTrue(result)
    }

    @Test
    fun should_return_false_when_path_does_not_match() {
        // Given
        val path = "example/path"
        val matcher = SimpleMatcherIgnore(path)

        // When
        val result = matcher.match("different/path")

        // Then
        assertFalse(result)
    }
}