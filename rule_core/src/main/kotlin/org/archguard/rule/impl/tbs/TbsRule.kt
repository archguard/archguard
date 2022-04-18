package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.SmellEmit

open class TbsRule : Rule() {
    override fun visit(rootNode: CodeDataStruct, context: RuleContext, callback: SmellEmit) {
        rootNode.Fields.forEachIndexed { index, it ->
            this.visitField(it, index, callback)
        }

        rootNode.Functions.forEachIndexed { index, it ->
            this.visitFunction(it, index, callback)

            it.Annotations.forEachIndexed { annotationIndex, annotation ->
                this.visitAnnotation(annotation, annotationIndex, callback)
            }

            it.FunctionCalls.forEachIndexed { callIndex, call ->
                this.visitFunctionCall(call, callIndex, callback)
            }
        }
    }

    open fun visitField(field: CodeField, index: Int, callback: SmellEmit) {}
    open fun visitFunction(function: CodeFunction, index: Int, callback: SmellEmit) {}
    open fun visitFunctionCall(codeCall: CodeCall, index: Int, callback: SmellEmit) {}
    open fun visitAnnotation(annotation: CodeAnnotation, index: Int, callback: SmellEmit) {}
}
