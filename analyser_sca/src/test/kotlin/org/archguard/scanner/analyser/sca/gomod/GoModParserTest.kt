package org.archguard.scanner.analyser.sca.gomod

import org.archguard.scanner.core.sca.DeclFileTree

internal class GoModParserTest {

    @org.junit.jupiter.api.Test
    fun basicLookupSource() {
        val declFileTree = DeclFileTree("coca", "god.md", modSample)
        val depDecls = GoModParser().lookupSource(declFileTree)
    }
}