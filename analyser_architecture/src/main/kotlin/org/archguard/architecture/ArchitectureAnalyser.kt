package org.archguard.architecture

import org.archguard.scanner.core.architecture.ArchitectureContext

class ArchitectureAnalyser(override val context: ArchitectureContext) :
    org.archguard.scanner.core.architecture.ArchitectureAnalyser {
    override fun analyse(): List<ArchitectureView> {
        // TODO: Implement this method
        return emptyList()
    }
}