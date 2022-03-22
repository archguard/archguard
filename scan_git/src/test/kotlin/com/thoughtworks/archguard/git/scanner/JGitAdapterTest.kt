package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class JGitAdapterTest {

    // this test would not pass in CI
    @Test
    @Disabled
    internal fun should_calculate_self() {
        val jGitAdapter = JGitAdapter(CognitiveComplexityParser())
        val (_, changeEntries) = jGitAdapter.scan("../", repoId = "0", systemId = 1)

        val changeCounts = jGitAdapter.countChangesByPath(changeEntries)
        assert(changeCounts[".github/workflows/cd.yaml"]!! >= 4)
    }
}