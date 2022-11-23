package org.archguard.scanner.analyser.sca.gomod

import org.archguard.scanner.core.sca.DeclFileTree

internal class GoModParserTest {

    @org.junit.jupiter.api.Test
    fun basicLookupSource() {
        val modSample = """module github.com/boyter/scc/v3

go 1.14

require (
	github.com/spf13/cobra v0.0.3
	github.com/spf13/pflag v1.0.3 // indirect
	golang.org/x/text v0.3.0
	gopkg.in/yaml.v2 v2.2.8
)
"""

        val declFileTree = DeclFileTree("coca", "mo.md", modSample)
        val depDecls = GoModParser().lookupSource(declFileTree)
    }
}