package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import com.thoughtworks.archguard.git.scanner.model.LineCounter
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class JGitAdapterTest {

    // this test would not pass in CI
    @Test
    @Disabled
    internal fun should_calculate_self() {
        val jGitAdapter = JGitAdapter(CognitiveComplexityParser(), "java")
        val (_, changeEntries) = jGitAdapter.scan("../", repoId = "0", systemId = 1)

        val changeCounts = jGitAdapter.countChangesByPath(changeEntries)
        assert(changeCounts[".github/workflows/cd.yaml"]!! >= 4)
    }

    @Test
    internal fun line_counts() {
        val resource = this.javaClass.classLoader.getResource("lines/hello.go")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        assert(LineCounter.byPath(path).toInt() == 7)
    }
}