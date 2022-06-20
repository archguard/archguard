package com.thoughtworks.archguard.insights.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class VersionNumberTest {

    @Test
    internal fun should_parse() {
        assertNull(VersionNumber.parse(""))
        assertEquals(12, VersionNumber.parse("12")!!.major)
    }

    @Test
    internal fun should_parse_with_minor() {
        assertEquals(VersionNumber(12, 25, 0, 0, null), VersionNumber.parse("12.25")!!)
        assertEquals(VersionNumber(12, 25, 23, 0, null), VersionNumber.parse("12.25.23")!!)
    }
}