package org.archguard.scanner.analyser.sca.gomod

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.DependencyEntry
import org.archguard.scanner.core.sca.PackageDependencies
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GoModParserTest {
    val modSample = """module github.com/boyter/scc/v3

go 1.14

require (
	github.com/spf13/cobra v0.0.3
	github.com/spf13/pflag v1.0.3 // indirect
	golang.org/x/text v0.3.0
	gopkg.in/yaml.v2 v2.2.8
    google.golang.org/protobuf v1.27.1 // indirect
)
"""

    @Test
    fun basicLookupSource() {
        val declFileTree = DeclFileTree("coca", "god.md", modSample)
        val depDecls = GoModParser().lookupSource(declFileTree)

        assertEquals(1, depDecls.size)
        assertEquals("github.com/boyter/scc/v3", depDecls[0].name)
        assertEquals("1.14", depDecls[0].version)

        val expect = PackageDependencies(
            "github.com/boyter/scc/v3", "1.14", "gomod", listOf(
                DependencyEntry("github.com/spf13/cobra", version = "0.0.3"),
                DependencyEntry("github.com/spf13/pflag", version = "1.0.3"),
                DependencyEntry("golang.org/x/text", version = "0.3.0"),
                DependencyEntry("gopkg.in/yaml.v2", version = "2.2.8"),
                DependencyEntry("google.golang.org/protobuf", version = "1.27.1"),
            ), "god.md"
        )

        assertEquals(Json.encodeToString(depDecls[0]), Json.encodeToString(expect))
    }
}