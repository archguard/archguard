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
}
