package org.archguard.scanner.analyser.sca.gradle;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class GradleTomlParserTest {

    @Test
    fun shouldParseGradleTomlFileAndReturnDependencyEntries() {
        // given
        val content = """
            [versions]
            groovy = "3.0.5"
            checkstyle = "8.37"

            [libraries]
            groovy-core = { module = "org.codehaus.groovy:groovy", version.ref = "groovy" }
            groovy-json = { module = "org.codehaus.groovy:groovy-json", version.ref = "groovy" }
            groovy-nio = { module = "org.codehaus.groovy:groovy-nio", version.ref = "groovy" }

            [bundles]
            groovy = ["groovy-core", "groovy-json", "groovy-nio"]

            [plugins]
            versions = { id = "com.github.ben-manes.versions", version = "0.45.0" }
        """.trimIndent()

        val parser = GradleTomlParser(content)

        // when
        val results = parser.parse()

        // then
        assertThat(results).isNotEmpty
        assertThat(results.size).isEqualTo(3)
        val result = results["groovy-core"]!!

        result.name shouldBe "groovy-core"
        result.group shouldBe "org.codehaus.groovy"
        result.artifact shouldBe "groovy"
        result.version shouldBe "3.0.5"
    }

    @Test
    fun shouldReturnEmptyListWhenGradleTomlFileIsEmpty() {
        // given
        val content = ""

        val parser = GradleTomlParser(content)

        // when
        val result = parser.parse()

        // then
        assertThat(result).isEmpty()
    }
}