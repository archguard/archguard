package org.archguard.scanner.ctl.impl

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class OfficialAnalyserSpecsTest {
    @Test
    fun `should output the spec with the default jar for all the official analysers`() {
        OfficialAnalyserSpecs.KOTLIN.spec().identifier shouldBe "kotlin"
        OfficialAnalyserSpecs.APICALLS.spec().identifier shouldBe "apicalls"
    }

    @Test
    fun `should output the spec with the default jar for javascript`() {
        OfficialAnalyserSpecs.JAVASCRIPT.spec().identifier shouldBe "typescript"
    }

    @Test
    fun `should output the jar file name for all the official analysers`() {
        OfficialAnalyserSpecs.JAVASCRIPT.jarFileName() shouldBe """lang_typescript-$ARCHGUARD_VERSION-all.jar"""
    }
}
