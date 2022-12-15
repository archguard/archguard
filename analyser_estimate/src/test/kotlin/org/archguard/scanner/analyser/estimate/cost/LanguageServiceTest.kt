package org.archguard.scanner.analyser.estimate.cost

import io.kotest.matchers.shouldBe
import org.archguard.scanner.analyser.count.LanguageService
import org.archguard.scanner.analyser.count.TokenType
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
    fun scan_shebang() {
        lang.scanForSheBang("#!  /usr/bin/env   perl   -w".toByteArray()) shouldBe "perl"
    }

    @Test
    fun determine_language_with_shebang() {
        val cases = listOf(
            "#!/usr/bin/perl",
            "#!  /usr/bin/perl",
            "#!/usr/bin/perl -w",
            "#!/usr/bin/env perl",
            "#!  /usr/bin/env   perl",
            "#!/usr/bin/env perl -w",
            "#!  /usr/bin/env   perl   -w",
            "#!/opt/local/bin/perl",
            "#!/usr/bin/perl5",
        )

        for (case in cases) {
            lang.detectSheBang(case) shouldBe "Perl"
        }
    }

    @Test
    fun match_csharp() {
        val feature = lang.getLanguageFeature("C#")
        val trieMatch = feature?.tokens?.match("for ".toByteArray())!!

        trieMatch.tokenType shouldBe TokenType.TComplexity
    }
}
