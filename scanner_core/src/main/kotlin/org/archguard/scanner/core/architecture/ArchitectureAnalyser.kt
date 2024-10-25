package org.archguard.scanner.core.architecture

import org.archguard.architecture.ArchitectureView
import org.archguard.scanner.core.Analyser

interface ArchitectureAnalyser : Analyser<ArchitectureContext> {
    fun analyse(): List<ArchitectureView>
}
