package com.thoughtworks.archgard.scanner.domain.scanner.git

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
internal class GitHotFileVOScannerTest(@Autowired val gitHotFileRepo: GitHotFileRepo) {

    private lateinit var spyGitHotFileScanner: GitHotFileScanner

    @BeforeEach
    fun setUp() {
        spyGitHotFileScanner = spyk(GitHotFileScanner(gitHotFileRepo))
    }
    
    @Test
    internal fun shouldSaveGitHotFileReport() {
        every { spyGitHotFileScanner.getHotFileReport(any()) } returns listOf(
                GitHotFileVO(1, "name1", 10),
                GitHotFileVO(1, "name2", 10)
        )
        
        spyGitHotFileScanner.scan(mockk())

        val findBySystemId = gitHotFileRepo.findBySystemId(1);
        
        assertNotNull(findBySystemId)
        assertEquals(2, findBySystemId.size)
        assertEquals(1, findBySystemId[0].systemId)
        assertEquals("name1", findBySystemId[0].name)
        assertEquals(10, findBySystemId[0].modifiedCount)
    }
}