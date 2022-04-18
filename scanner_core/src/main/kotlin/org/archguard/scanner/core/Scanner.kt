package org.archguard.scanner.core

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.context.Context

interface Scanner<C : Context> {
    val context: Context
}

interface LanguageScanner<C : Context> : Scanner<C> {
    fun execute(): List<CodeDataStruct>
}

interface FeatureScanner<C : Context> : Scanner<C> {
    fun execute(input: Any): Any?
}
