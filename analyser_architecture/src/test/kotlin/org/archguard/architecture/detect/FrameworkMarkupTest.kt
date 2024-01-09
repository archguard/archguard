package org.archguard.architecture.detect

import org.archguard.architecture.techstack.FrameworkMarkup
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class FrameworkMarkupTest {
    @Test
    internal fun serial_json() {
        val markups = FrameworkMarkup.fromResource()
        assertEquals("Java", markups[0].language)
    }

    @Test
    internal fun extend_api_for_kotlin_extend_java() {
        val markups = FrameworkMarkup.fromResource()
        assertEquals("Kotlin", markups[1].language)
        assert(markups[1].appTypeMapping.isNotEmpty())
    }

    @Test
    internal fun by_language() {
        val markup = FrameworkMarkup.byLanguage("Kotlin")
        assert(markup!!.appTypeMapping.isNotEmpty())
    }
}