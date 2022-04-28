package org.archguard.architecture.detect

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CodeArchitectureMarkupTest {
    @Test
    internal fun serial_json() {
        val markups = CodeArchitectureMarkup.fromResource()
        assertEquals("Java", markups[0].language)
    }

    @Test
    internal fun extend_api_for_kotlin_extend_java() {
        val markups = CodeArchitectureMarkup.fromResource()
        assertEquals("Kotlin", markups[1].language)
        assert(markups[1].appTypes.isNotEmpty())
    }
}