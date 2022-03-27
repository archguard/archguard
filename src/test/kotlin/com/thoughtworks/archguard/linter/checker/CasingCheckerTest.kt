package com.thoughtworks.archguard.linter.checker

import org.junit.jupiter.api.Test

internal class CasingCheckerTest {
    @Test
    fun testPascalUrl() {
        assert(CasingChecker().isPacal("PascalNaming"))
        assert(CasingChecker().isKebab("kebab-naming"))
        assert(CasingChecker().isSnake("snake_naming"))
        assert(CasingChecker().isCOBOL("SNAKE-NAMING"))
        assert(CasingChecker().isMacro("MACRO_NAMING"))
        assert(CasingChecker().isCamel("camelNaming"))
    }
}