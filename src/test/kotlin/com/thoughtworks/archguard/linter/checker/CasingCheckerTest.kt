package com.thoughtworks.archguard.linter.checker

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CasingCheckerTest {
    @Test
    fun testPascalUrl() {
        assert(CasingChecker().IsPacal("PascalNaming"))
        assert(CasingChecker().`is-kebab`("kebab-naming"))
        assert(CasingChecker().is_nake("snake_naming"))
        assert(CasingChecker().`IS-COBOL`("SNAKE-NAMING"))
        assert(CasingChecker().IS_MACRO("MACRO_NAMING"))
        assert(CasingChecker().isCamel("camelNaming"))
    }

    @Test
    fun checkMultipleNamingStyle() {
        val url = "/api/demo-api/MACRO".split("/")
        assertEquals(4, CasingChecker().checkNaming(url).size)
    }
}