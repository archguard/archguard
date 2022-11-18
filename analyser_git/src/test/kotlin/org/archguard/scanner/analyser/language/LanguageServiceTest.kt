package org.archguard.scanner.analyser.language

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LanguageServiceTest {
    val lang = LanguageService()

    @Test
    fun simple_detect() {
        Assertions.assertEquals("License", lang.determineLanguage("LICENSE"))
        Assertions.assertEquals("gitignore", lang.determineLanguage(".gitignore"))
        Assertions.assertEquals("TypeScript Typings", lang.determineLanguage("types.d.ts"))
        Assertions.assertEquals("YAML", lang.determineLanguage(".travis.yml"))
        Assertions.assertEquals("d.ts", lang.getExtension("types.d.ts"))
        Assertions.assertEquals("JSON", lang.determineLanguage("api.json"))
        Assertions.assertEquals("", lang.determineLanguage("logo.png"))
    }
}
