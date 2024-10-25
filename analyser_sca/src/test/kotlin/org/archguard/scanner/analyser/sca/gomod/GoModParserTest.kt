package org.archguard.scanner.analyser.sca.gomod

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.model.DeclFileTree
import org.archguard.model.DependencyEntry
import org.archguard.model.PackageDependencies
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
                DependencyEntry("github.com/spf13/cobra", "github.com/spf13", "cobra", "0.0.3"),
                DependencyEntry("github.com/spf13/pflag", "github.com/spf13", "pflag", "1.0.3"),
                DependencyEntry("golang.org/x/text", "golang.org/x", "text", "0.3.0"),
                DependencyEntry("gopkg.in/yaml.v2", "gopkg.in", "yaml.v2", "2.2.8"),
                DependencyEntry("google.golang.org/protobuf", "google.golang.org", "protobuf", "1.27.1")
            ), "god.md"
        )

        assertEquals(Json.encodeToString(depDecls[0]), Json.encodeToString(expect))
    }
}