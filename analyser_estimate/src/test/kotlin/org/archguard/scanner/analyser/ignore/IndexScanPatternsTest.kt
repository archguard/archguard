package org.archguard.scanner.analyser.ignore;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IndexScanPatternsTest {

    @Test
    fun `given path and isDir, should return true if absolute holder matches`() {
        // given
        val absoluteHolder = DepthPatternHolder(Order.ASC)
        val relativeHolder = DepthPatternHolder(Order.DESC)
        val indexScanPatterns = IndexScanPatterns(absoluteHolder, relativeHolder)
        val path = "/path/to/file"
        val isDir = false
        absoluteHolder.add("/path/to/*")

        // when
        val result = indexScanPatterns.match(path, isDir)

        // then
        assertFalse(result)
    }

    @Test
    fun `given path and isDir, should return true if relative holder matches`() {
        // given
        val absoluteHolder = DepthPatternHolder(Order.ASC)
        val relativeHolder = DepthPatternHolder(Order.DESC)
        val indexScanPatterns = IndexScanPatterns(absoluteHolder, relativeHolder)
        val path = "path/to/file"
        val isDir = false
        relativeHolder.add("path/to/*")

        // when
        val result = indexScanPatterns.match(path, isDir)

        // then
        assertTrue(result)
    }

    @Test
    fun `given path and isDir, should return false if neither absolute nor relative holder matches`() {
        // given
        val absoluteHolder = DepthPatternHolder(Order.ASC)
        val relativeHolder = DepthPatternHolder(Order.DESC)
        val indexScanPatterns = IndexScanPatterns(absoluteHolder, relativeHolder)
        val path = "/path/to/file"
        val isDir = false

        // when
        val result = indexScanPatterns.match(path, isDir)

        // then
        assertFalse(result)
    }
}