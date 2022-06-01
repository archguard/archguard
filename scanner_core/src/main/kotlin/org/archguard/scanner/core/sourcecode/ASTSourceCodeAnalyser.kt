package org.archguard.scanner.core.sourcecode

import chapi.domain.core.CodeDataStruct

interface ASTSourceCodeAnalyser : SourceCodeAnalyser {
    override fun analyse(input: Any?): List<Any>? {
        return analyse(input as List<CodeDataStruct>)
    }

    fun analyse(input: List<CodeDataStruct>): List<Any>?
}
