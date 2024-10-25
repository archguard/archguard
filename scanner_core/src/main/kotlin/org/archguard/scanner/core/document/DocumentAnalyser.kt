package org.archguard.scanner.core.document

import org.archguard.context.DocumentChunk
import org.archguard.scanner.core.Analyser

interface DocumentAnalyser : Analyser<DocumentContext> {
    fun analyse(): List<DocumentChunk>
}