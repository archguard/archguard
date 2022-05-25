package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import java.io.File

internal class GitHotFileScannerToolTest {

    @Test
    internal fun shouldGetGitHotFileModifiedCountMap() {
        val consumer = InMemoryConsumer()
        val spyGitHotFileScannerTool =
            spyk(GitHotFileScannerTool(mockk(), "master", consumer), recordPrivateCalls = true)
        every { spyGitHotFileScannerTool["getGitHotFileCommitFile"]() } returns File(
            javaClass.classLoader.getResource("TestGitHotFile/scm_git_hot_file.txt").toURI()
        )
        val map = spyGitHotFileScannerTool.getGitHotFileModifiedCountMap()

        kotlin.test.assertNotEquals(0, map.size)
        kotlin.test.assertEquals(4, map["src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java"])
    }
}
