package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.model.LineCounter
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class JGitAdapterTest {
    @Test
    internal fun should_calculate_self() {
        val localPath = File("./build/ddd")

        // if test in local, skip clone
        if (!File(localPath, ".git").isDirectory) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(localPath)
                .call()
        }

        val jGitAdapter = JGitAdapter("java")
        val (_, changeEntries) = jGitAdapter.scan("./build/ddd", repoId = "0", systemId = 1)

        val changeCounts = jGitAdapter.countChangesByPath(changeEntries)
        assert(changeCounts["README.md"]!! >= 2)
    }

    @Test
    internal fun line_counts() {
        val resource = this.javaClass.classLoader.getResource("lines/hello.go")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        assert(LineCounter.byPath(path).toInt() == 7)
    }
}