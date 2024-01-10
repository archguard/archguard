package com.thoughtworks.archguard.scanner.infrastructure.command

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ScannerCommandTest {

    @Test
    fun cmd_test() {
        val command = ScannerCommand()
            .file("hello.jar")
            .dbUrl("localhost")
            .path(".")
            .systemId("2")
            .language("java")

        assertEquals("java -jar -Ddburl=localhost?useSSL=false hello.jar --path=. --system-id=2 --language=java", command.getCommand().joinToString(" "))
    }

    @Test
    fun shouldReturnJavaCommand() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.getCommand()

        // Then
        assertEquals("java", command[0])
    }

    @Test
    fun shouldSetFileName() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.file("scanner.jar").getCommand()

        // Then
        assertEquals("scanner.jar", command[3])
    }

    @Test
    fun shouldSetDbUrl() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.dbUrl("jdbc:mysql://localhost:3306/mydb").getCommand()

        // Then
        assertEquals("-Ddburl=jdbc:mysql://localhost:3306/mydb?useSSL=false", command[2])
    }

    @Test
    fun shouldSetPath() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.path("/path/to/files").getCommand()

        // Then
        assertEquals("--path=/path/to/files", command[4])
    }

    @Test
    fun shouldSetSystemId() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.systemId("12345").getCommand()

        // Then
        assertEquals("--system-id=12345", command[4])
    }

    @Test
    fun shouldSetLanguage() {
        // Given
        val scannerCommand = ScannerCommand()

        // When
        val command = scannerCommand.language("Java").getCommand()

        // Then
        assertEquals("--language=java", command[4])
    }
}
