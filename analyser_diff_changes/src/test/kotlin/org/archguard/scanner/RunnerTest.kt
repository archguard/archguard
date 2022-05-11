package org.archguard.scanner.diffchanges

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.archguard.scanner.analyser.DiffChangesAnalyser
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.DiffChangesContext
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DiffChangesAnalyserTest {
    private val mockClient = mockk<ArchGuardClient> {
        every { saveDiffs(any()) } just runs
    }
    private val mockContext = mockk<DiffChangesContext> {
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

    @Test
    fun `should return the number of commits in the repository`() {
        every { mockContext.path } returns targetRepo.absolutePath
        every { mockContext.branch } returns "master"
        every { mockContext.since } returns "5952edc"
        every { mockContext.until } returns "f3fb4e2"
        every { mockContext.depth } returns 7

        val analyser = DiffChangesAnalyser(mockContext)
        val changes = analyser.analyse()

        assertThat(changes).hasSize(3)
        assertThat(changes[0].relations).hasSize(1)

        val relation = changes[0].relations.first()

        assertThat(relation.source).isEqualTo("com.dmall.productservice.apis.ProductController.getAllProducts")
        assertThat(relation.target).isEqualTo("com.dmall.productservice.apis.assembler.ProductAssembler.toProductResponseList")
    }
}
