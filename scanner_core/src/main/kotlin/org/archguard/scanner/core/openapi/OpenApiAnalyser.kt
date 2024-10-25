package org.archguard.scanner.core.openapi

import org.archguard.model.ApiCollection
import org.archguard.scanner.core.Analyser

interface OpenApiAnalyser: Analyser<OpenApiContext> {
    fun analyse(): List<ApiCollection>
}