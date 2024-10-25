package org.archguard.scanner.core.sca

import org.archguard.context.CompositionDependency
import org.archguard.scanner.core.Analyser

interface ScaAnalyser : Analyser<ScaContext> {
    fun analyse(): List<CompositionDependency>
}
