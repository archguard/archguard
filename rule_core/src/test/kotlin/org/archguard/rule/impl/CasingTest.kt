package org.archguard.rule.impl

import org.archguard.rule.impl.common.Casing
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CasingTest {
    @Test
    fun testPascalUrl() {
        assert(Casing().IsPacal("PascalNaming"))
        assert(Casing().`is-kebab`("kebab-naming"))
        assert(Casing().is_nake("snake_naming"))
        assert(Casing().`IS-COBOL`("SNAKE-NAMING"))
        assert(Casing().IS_MACRO("MACRO_NAMING"))
        assert(Casing().isCamel("camelNaming"))
    }

    @Test
    fun checkMultipleNamingStyle() {
        val url = "/api/demo-api/MACRO".split("/")
        val namings = Casing().checkNaming(url)
        assertEquals(2, namings.size)
        assertEquals("macro, kebab", namings.keys.joinToString(", "))
    }
}
