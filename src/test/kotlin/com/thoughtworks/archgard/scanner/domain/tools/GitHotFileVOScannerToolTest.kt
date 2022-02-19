package com.thoughtworks.archgard.scanner.domain.tools

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class GitHotFileVOScannerToolTest {
    
    @Test
    internal fun shouldGetGitHotFileModifiedCountMap() {
        val spyGitHotFileScannerTool = spyk(GitHotFileScannerTool(mockk(), "master"), recordPrivateCalls = true)
        every { spyGitHotFileScannerTool["getGitHotFileCommitFile"]() } returns File(javaClass.classLoader.getResource("TestGitHotFile/git_hot_file.txt").toURI())
        val map = spyGitHotFileScannerTool.getGitHotFileModifiedCountMap()
        
        assertNotEquals(0, map.size)
        assertEquals(4, map["src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java"])
    }
}