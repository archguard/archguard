package org.archguard.scanner.cost.estimate.cost

import io.kotest.matchers.shouldBe
import org.archguard.scanner.cost.count.LanguageService
import org.archguard.scanner.cost.count.TokenType
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class LanguageServiceTest {

    // setup
    private val lang = LanguageService()

    @Test
    fun determine_language() {
        lang.determineLanguage("", listOf("Coq", "SystemVerilog"), "Require Hypothesis Inductive".toByteArray()) shouldBe "Coq"
        lang.determineLanguage("", listOf("Coq", "SystemVerilog"), "endmodule posedge edge always wire".toByteArray()) shouldBe "SystemVerilog"
        lang.determineLanguage("Java", listOf(), "endmodule posedge edge always wire".toByteArray()) shouldBe "Java"
    }

    @Test
    fun determine_language_with_content() {
        val possibleLanguages = lang.detectLanguages(".travis.yml");
        lang.determineLanguage(possibleLanguages[0], possibleLanguages, """Resources:
  MyEC2Instance: #An inline comment
    Type: "AWS::EC2::Instance"""".toByteArray()) shouldBe "CloudFormation (YAML)"
    }

    @Test
    @Disabled
    fun determine_language_with_shebang() {
        lang.determineLanguage("", listOf("Coq", "SystemVerilog"), "#!/usr/bin/env coqtop".toByteArray()) shouldBe "Coq"
        lang.determineLanguage("", listOf("Coq", "SystemVerilog"), "#!/usr/bin/env verilog".toByteArray()) shouldBe "SystemVerilog"
    }

    @Test
    fun match_csharp() {
        val feature = lang.getLanguageFeature("C#")
        val trieMatch = feature?.tokens?.match("for ".toByteArray())!!

        trieMatch.tokenType shouldBe TokenType.TComplexity
    }
}
