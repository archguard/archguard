package org.archguard.scanner.analyser

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.git.GitContext
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.*
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GitAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveGitLogs(any()) } just runs
    }
    private val mockContext = mockk<GitContext> {
        every { client } returns mockClient
    }
    private lateinit var targetRepo: File

    @BeforeAll
    fun setup() {
        targetRepo = File("./build/ddd")
        if (!targetRepo.exists()) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(targetRepo)
                .call()
        }
    }

    @AfterEach
    internal fun tearDown() {
        verify { mockClient.saveGitLogs(any()) }
    }

    @Test
    fun `should return the number of commits in the repository`() {
        every { mockContext.path } returns targetRepo.absolutePath
        every { mockContext.branch } returns "master"
        every { mockContext.repoId } returns "ddd-monolithic-code-sample"
        every { mockContext.startedAt } returns 0

        val analyser = GitAnalyser(mockContext)
        val gitLogs = analyser.analyse()[0]

        assertThat(gitLogs.commitLog).hasSize(8)
        assertThat(gitLogs.changeEntry).hasSize(69)
        assertThat(gitLogs.pathChangeCount).hasSize(36)
    }
}
