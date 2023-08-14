package org.archguard.scanner.analyser.api

import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sca.ScaAnalyser
import org.archguard.scanner.core.sca.ScaContext

class OpenApiAnalyser(override val context: ScaContext) : ScaAnalyser {
    private val client = context.client
    private val path = context.path

    override fun analyse(): List<CompositionDependency> {
        TODO("Not yet implemented")
    }

}