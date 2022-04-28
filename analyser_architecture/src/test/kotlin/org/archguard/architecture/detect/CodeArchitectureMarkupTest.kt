package org.archguard.architecture.detect

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CodeArchitectureMarkupTest {
    @Test
    internal fun serial() {
        val markups = CodeArchitectureMarkup.fromResource()
        assertEquals("Java", markups[0].language)
    }
}