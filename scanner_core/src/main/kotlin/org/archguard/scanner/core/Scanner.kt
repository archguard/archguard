package org.archguard.scanner.core

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.context.Context

interface Scanner<C : Context> {
    val context: Context
}

// first level scanner
interface LanguageScanner<C : Context> : Scanner<C> {
    fun execute(): List<CodeDataStruct>
}

// chained scanner
interface FeatureScanner<C : Context> : Scanner<C> {
    fun execute(input: Any): Any?
}
