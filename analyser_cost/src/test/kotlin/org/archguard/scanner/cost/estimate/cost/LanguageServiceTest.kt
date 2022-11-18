package org.archguard.scanner.cost.estimate.cost

import io.kotest.matchers.shouldBe
import org.archguard.scanner.cost.count.LanguageService
import org.archguard.scanner.cost.count.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LanguageServiceTest {

    // setup
    private val lang = LanguageService()

    @Test
    fun should_return_LICENSE_when_is_a_license_file() {
        Assertions.assertEquals("License", lang.determineLanguage("LICENSE"))
        Assertions.assertEquals("gitignore", lang.determineLanguage(".gitignore"))
        Assertions.assertEquals("TypeScript Typings", lang.determineLanguage("types.d.ts"))
        Assertions.assertEquals("YAML", lang.determineLanguage(".travis.yml"))
        Assertions.assertEquals("d.ts", lang.getExtension("types.d.ts"))
        Assertions.assertEquals("JSON", lang.determineLanguage("api.json"))
        Assertions.assertEquals("", lang.determineLanguage("logo.png"))
    }

    @Test
    fun match_csharp() {
        val feature = lang.getLanguageFeature("C#")
        val trieMatch = feature?.tokens?.match("for ".toByteArray())!!

        trieMatch.tokenType shouldBe TokenType.TComplexity
    }
}
