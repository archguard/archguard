package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import org.junit.jupiter.api.Test

class GitFileChange(
    val creationDate: String,
)

internal class JGitAdapterTest {

    @Test
    internal fun should_calculate_self() {
        val jGitAdapter = JGitAdapter(CognitiveComplexityParser())
        val (commitLogs, changeEntries) = jGitAdapter.scan("../", repoId = "0", systemId = 1)

        changeEntries.forEach {

        }
    }
}