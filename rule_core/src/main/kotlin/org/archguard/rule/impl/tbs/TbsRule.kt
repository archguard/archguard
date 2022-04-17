package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.core.Rule

open class TbsRule: Rule() {
    override fun visit(rootNode: CodeDataStruct, callback: SmellEmit) {
        rootNode.Fields.forEachIndexed { index, it ->
            this.visitField(it, index)
        }

        rootNode.Functions.forEachIndexed { index, it ->
            this.visitFunction(it, index)

            it.FunctionCalls.forEachIndexed { callIndex, call ->
                this.visitFunctionCall(call, callIndex)
            }
        }
    }

    open fun visitFunctionCall(codeCall: CodeCall, index: Int) {}
    open fun visitFunction(function: CodeFunction, index: Int) {}
    open fun visitField(field: CodeField, index: Int) {}
}