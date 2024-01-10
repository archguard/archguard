package org.archguard.doc.generator.compiler;

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DocMessageCollectorTest {

    @Test
    fun shouldClearSeenErrors() {
        // Given
        val logger: Logger = LoggerFactory.getLogger(DocMessageCollector::class.java)
        val messageCollector = DocMessageCollector(logger)
        messageCollector.report(CompilerMessageSeverity.ERROR, "Error message", null)

        // When
        messageCollector.clear()

        // Then
        assert(!messageCollector.hasErrors())
    }

    @Test
    fun shouldReportErrorAndSetSeenErrorsToTrue() {
        // Given
        val logger: Logger = LoggerFactory.getLogger(DocMessageCollector::class.java)
        val messageCollector = DocMessageCollector(logger)

        // When
        messageCollector.report(CompilerMessageSeverity.ERROR, "Error message", null)

        // Then
        assert(messageCollector.hasErrors())
    }

    @Test
    fun shouldNotSetSeenErrorsToTrueForNonErrorSeverity() {
        // Given
        val logger: Logger = LoggerFactory.getLogger(DocMessageCollector::class.java)
        val messageCollector = DocMessageCollector(logger)

        // When
        messageCollector.report(CompilerMessageSeverity.WARNING, "Warning message", null)

        // Then
        assert(!messageCollector.hasErrors())
    }
}

