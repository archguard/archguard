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

    @Test
    internal fun should_parse_with_slash() {
        assertEquals(VersionNumber(1, 0, 0, 0, "rc1-SNAPSHOT"), VersionNumber.parse("1-rc1-SNAPSHOT")!!)
        assertEquals(VersionNumber(1, 2, 0, 0, "rc1-SNAPSHOT"), VersionNumber.parse("1.2-rc1-SNAPSHOT")!!)
        assertEquals(VersionNumber(1, 2, 3, 0, "rc1-SNAPSHOT"), VersionNumber.parse("1.2.3-rc1-SNAPSHOT")!!)
        assertEquals(VersionNumber(1, 2, 3, 4, null), VersionNumber.parse("1.2.3_4")!!)
    }

    @Test
    internal fun should_parse_with_qualifier() {
        assertEquals(VersionNumber(1, 2, 3, 0, "rc1-SNAPSHOT"), VersionNumber.parse("1.2.3.rc1-SNAPSHOT")!!)
    }


    @Test
    internal fun compare() {
        assert(VersionNumber.parse("1.2.3.rc1-SNAPSHOT")!! > VersionNumber.parse("1.2.2.rc1-SNAPSHOT")!!)
    }
}