package org.archguard.scanner.analyser

import org.archguard.scanner.core.document.DocumentContext
import org.archguard.scanner.core.document.DocumentLang

class DocumentAnalyser(override val context: DocumentContext) : org.archguard.scanner.core.document.DocumentAnalyser {
    override fun analyse(): List<DocumentLang> {
        return listOf()
    }

}