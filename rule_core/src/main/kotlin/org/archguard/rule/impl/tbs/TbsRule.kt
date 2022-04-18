package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.core.SmellEmit
import org.archguard.rule.core.SmellPosition

fun CodePosition.smellPosition(): SmellPosition {
    return SmellPosition(
        startLine = this.StartLine,
        startColumn = this.StartLinePosition,
        endLine = this.StopLine,
        endColumn = this.StopLinePosition
    )
}

open class TbsRule(
    var language: TbsLanguage = TbsLanguage.JAVA
) : Rule() {
    override fun visit(rootNode: CodeDataStruct, context: RuleContext, callback: SmellEmit) {
        rootNode.Fields.forEachIndexed { index, it ->
            this.visitField(it, index, callback)
        }

        rootNode.Functions.forEachIndexed { index, it ->
            // todo: condition by languages
            val testsFilter = it.Annotations.filter { it.Name == "Test" || it.Name.endsWith(".test") }
            val isTest = testsFilter.isNotEmpty()

            if (isTest) {
                this.visitFunction(it, index, callback)

                it.Annotations.forEachIndexed { annotationIndex, annotation ->
                    this.visitAnnotation(it, annotation, annotationIndex, callback)
                }


                val currentMethodCalls =
                    addExtractAssertMethodCall(it, rootNode, (context as TestSmellContext).methodMap)

                currentMethodCalls.forEachIndexed { callIndex, call ->
                    this.visitFunctionCall(it, call, callIndex, callback)
                }
            }
        }
    }

    private fun addExtractAssertMethodCall(
        method: CodeFunction,
        node: CodeDataStruct,
        callMethodMap: MutableMap<String, CodeFunction>
    ): Array<CodeCall> {
        var methodCalls = method.FunctionCalls
        for (methodCall in methodCalls) {
            if (methodCall.NodeName == node.NodeName) {
                val mapFunc = callMethodMap[methodCall.buildFullMethodName()]
                if (mapFunc != null && mapFunc.Name != "") {
                    methodCalls += mapFunc.FunctionCalls
                }
            }
        }

        return methodCalls
    }

    open fun visitField(field: CodeField, index: Int, callback: SmellEmit) {}
    open fun visitFunction(function: CodeFunction, index: Int, callback: SmellEmit) {}
    open fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: SmellEmit) {}
    open fun visitAnnotation(function: CodeFunction, annotation: CodeAnnotation, index: Int, callback: SmellEmit) {}
}
