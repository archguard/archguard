package org.archguard.scanner.core.document

import org.archguard.model.DocumentChunk
import org.archguard.scanner.core.Analyser

interface DocumentAnalyser : Analyser<DocumentContext> {
    fun analyse(): List<DocumentChunk>
}