package org.archguard.domain.version

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ComparisonTest {

    @Test
    internal fun comparison_from_string() {
        assertEquals(Comparison.GreaterThanOrEqual, Comparison.fromString(">="))
        assertEquals(Comparison.GreaterThan, Comparison.fromString(">"))
        assertEquals(Comparison.LessThanOrEqual, Comparison.fromString("<="))
        assertEquals(Comparison.LessThan, Comparison.fromString("<"))
        assertEquals(Comparison.Equal, Comparison.fromString("=="))
        assertEquals(Comparison.NotEqual, Comparison.fromString("!="))

        assertEquals(Comparison.NotSupport, Comparison.fromString(""))
    }
}