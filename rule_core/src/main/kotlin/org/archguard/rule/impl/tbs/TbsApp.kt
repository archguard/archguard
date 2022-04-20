package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct

class TbsApp {
    fun run(codeDataStructs: Array<CodeDataStruct>) {
        val provider = TestSmellProvider()
        val visitor = TestSmellVisitor(codeDataStructs)

        codeDataStructs.map {
            visitor
                .visitor(listOf(provider.get()), it)
        }
    }
}