package org.archguard.scanner.core.architecture

import org.archguard.scanner.core.Analyser

interface ArchitectureAnalyser : Analyser<ArchitectureContext> {
    fun analyse(language: String): List<Any>
}
