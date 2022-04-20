package org.archguard.scanner.core.sourcecode

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.Analyser

interface SourceCodeAnalyser : Analyser<SourceCodeContext> {
    fun analyse(input: Any?): Any?
}

// 重载函数, 实现细粒度的接口定义
interface FrontierSourceCodeAnalyser : SourceCodeAnalyser {
    override fun analyse(input: Any?): Any? {
        return analyse()
    }

    fun analyse(): List<CodeDataStruct>
}

interface ASTSourceCodeAnalyser : SourceCodeAnalyser {
    override fun analyse(input: Any?): Any? {
        return analyse(input as List<CodeDataStruct>)
    }

    fun analyse(input: List<CodeDataStruct>): Any?
}
