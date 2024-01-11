package com.thoughtworks.archguard.scanner.domain.scanner.javaext.pmd;

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.domain.system.BuildTool.*
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.scanner.infrastructure.db.PmdRepository
import org.dom4j.Element
import org.dom4j.dom.DOMDocument
import org.dom4j.io.SAXReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class PmdScannerTest {
    private val pmdRepository = mock(PmdRepository::class.java)
    private val pmdScanner = PmdScanner(pmdRepository)
    private val log: Logger = LoggerFactory.getLogger(PmdScanner::class.java)

    @Test
    fun `should return correct scanner name`() {
        val expected = "pmd"
        val actual = pmdScanner.getScannerName()
        assertEquals(expected, actual)
    }
    val context = ScanContext(
        systemId = 12345,
        repo = "my-repo",
        buildTool = GRADLE,
        workspace = File("/path/to/workspace"),
        dbUrl = "jdbc:mysql://localhost:3306/mydb",
        config = listOf(),
        language = "java",
        codePath = "/path/to/code",
        branch = "main",
        logStream = InMemoryConsumer(),
        additionArguments = listOf("--arg1", "--arg2"),
        scannerVersion = "1.0.0"
    )

    @Test
    fun shouldReturnScannerName() {
        // Given
        val pmdRepository = mock(PmdRepository::class.java)
        val pmdScanner = PmdScanner(pmdRepository)

        // When
        val scannerName = pmdScanner.getScannerName()

        // Then
        assertEquals("pmd", scannerName)
    }

    @Test
    fun shouldScanAndSaveViolations() {
        // Given
        val pmdRepository = mock(PmdRepository::class.java)
        val pmdScanner = PmdScanner(pmdRepository)

        // When
        pmdScanner.scan(context)

        // Then
        verify(pmdRepository, times(1)).clean()
        verify(pmdRepository, times(1)).save(anyList())
    }
}
