package org.archguard.scanner.estimate.ignore

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FilepathMatcherIgnoreTest {
    @Test
    fun convertGlobToRegexTest() {
        convert("*.kt") shouldBe "^.*\\.kt\$"
        convert("*.kt?") shouldBe "^.*\\.kt.\$"
        convert("*.kt*") shouldBe "^.*\\.kt.*$"
        convert("!build/demo.kt") shouldBe "^!build/demo\\.kt\$"
        convert("build/demo.kt") shouldBe "^build/demo\\.kt\$"
    }

    private fun convert(text: String) = FilepathMatcherIgnore.convertGlobToRegex(text)

    @Test
    fun shouldConvertPathMatcherWithDir() {
        val matcher = FilepathMatcherIgnore("build/*.kt")

        matcher.match("build/Pattern.kt") shouldBe true
        matcher.match("build/Pattern.java") shouldBe false
    }
}