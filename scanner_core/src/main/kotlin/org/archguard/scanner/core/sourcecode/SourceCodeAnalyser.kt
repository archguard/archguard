package org.archguard.scanner.core.sourcecode

import org.archguard.scanner.core.Analyser

/**
 * SourceCodeAnalyser is an interface for analysing source code.
 */
interface SourceCodeAnalyser : Analyser<SourceCodeContext> {
    fun analyse(input: Any?): List<Any>?
}
