package org.archguard.scanner.core.sourcecode

import org.archguard.scanner.core.Analyser

interface SourceCodeAnalyser : Analyser<SourceCodeContext> {
    fun analyse(input: Any?): List<Any>?
}
