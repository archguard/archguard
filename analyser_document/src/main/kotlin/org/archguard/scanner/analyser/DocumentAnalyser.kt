package org.archguard.scanner.analyser

import org.archguard.scanner.core.document.DocumentContext
import org.archguard.scanner.core.document.DocumentChunk

class DocumentAnalyser(override val context: DocumentContext) : org.archguard.scanner.core.document.DocumentAnalyser {
    override fun analyse(): List<DocumentChunk> {
        return listOf()
    }
}