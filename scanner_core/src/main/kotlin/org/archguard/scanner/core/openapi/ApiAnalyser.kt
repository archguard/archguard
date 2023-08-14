package org.archguard.scanner.core.openapi

import org.archguard.scanner.core.Analyser

interface ApiAnalyser: Analyser<ApiContext> {
    fun analyse(): List<ApiCollection>
}