package org.archguard.domain.version

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class VersionCompareTest {

    @Test
    internal fun simple_splice() {
        val comparison = VersionCompare.parse(">= 12.4")
        assertEquals(VersionCompare(">=", "12.4"), comparison)
    }
}