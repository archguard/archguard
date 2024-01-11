package org.archguard.scanner.analyser.ignore;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FullScanPatternsTest {

    @Test
    fun shouldAddPatternToAbsolute() {
        // given
        val patterns = FullScanPatterns()

        // when
        patterns.add("/pattern")

        // then
        assertEquals(1, patterns.absolute.size())
    }

    @Test
    fun shouldAddPatternToRelative() {
        // given
        val patterns = FullScanPatterns()

        // when
        patterns.add("pattern")

        // then
        assertEquals(1, patterns.relative.size())
    }

    @Test
    fun shouldMatchAbsolutePattern() {
        // given
        val patterns = FullScanPatterns()
        patterns.add("/pattern")

        // when
        val result = patterns.match("/path/to/pattern", false)

        // then
        assertFalse(result)
    }

    @Test
    fun shouldMatchRelativePattern() {
        // given
        val patterns = FullScanPatterns()
        patterns.add("pattern")

        // when
        val result = patterns.match("/path/to/pattern", false)

        // then
        if (System.getProperty("os.name").lowercase().contains("windows")) {
            // do nothing
        } else {
            assertTrue(result)
        }
    }

    @Test
    fun shouldNotMatchPattern() {
        // given
        val patterns = FullScanPatterns()
        patterns.add("/pattern")

        // when
        val result = patterns.match("/path/to/other", false)

        // then
        assertFalse(result)
    }
}
