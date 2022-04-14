package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner2.domain.model.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
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
internal class GitHotFileScannerTest(@Autowired val gitHotFileRepo: ScannerGitHotFileRepo) {

    private lateinit var spyGitHotFileScanner: GitHotFileScanner
    private lateinit var jClassRepository: JClassRepository

    @BeforeEach
    fun setUp() {
        jClassRepository = mockk<JClassRepository>()
        every { jClassRepository.findClassBy(any(), any(), any()) } returns JClass("1", "", "")
        spyGitHotFileScanner = spyk(GitHotFileScanner(gitHotFileRepo, jClassRepository))
    }

    @Test
    internal fun shouldSaveGitHotFileReport() {
        every { spyGitHotFileScanner.getHotFileReport(any()) } returns listOf(
            GitHotFileVO("src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", 4),
            GitHotFileVO("src/test/java/com/qicaisheng/parkinglot/ParkingDirectorTest.java", 14)
        )

        val scanContext = mockk<ScanContext>()
        every { scanContext.systemId } returns 1
        every { scanContext.repo } returns "repo1"
        spyGitHotFileScanner.scan(scanContext)

        val findBySystemId = gitHotFileRepo.findBySystemId(1)

        assertNotNull(findBySystemId)
        assertEquals(2, findBySystemId.size)
        assertEquals("src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", findBySystemId[0].path)
        assertEquals("com.qicaisheng.parkinglot.HTMLReportVisitor", findBySystemId[0].className)
        assertEquals(null, findBySystemId[0].moduleName)
        assertEquals(4, findBySystemId[0].modifiedCount)
        assertEquals("1", findBySystemId[0].jclassId)
    }
}
