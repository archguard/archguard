package org.archguard.linter.rule.testcode

import chapi.domain.core.*
import org.archguard.rule.common.Language
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext

fun CodePosition.smellPosition(): IssuePosition {
    return IssuePosition(
        startLine = this.StartLine,
        startColumn = this.StartLinePosition,
        endLine = this.StopLine,
        endColumn = this.StopLinePosition
    )
}

fun CodePosition.positionWith(funcName: String): IssuePosition {
    return IssuePosition(
        startLine = this.StartLine,
        startColumn = this.StartLinePosition,
        endLine = this.StopLine,
        endColumn = this.StopLinePosition,
        additions = mapOf("methodName" to funcName)
    )
}


val ASSERTION_LIST = arrayOf(
    "assert",
    "should",
    "check",    // ArchUnit,
    "maynotbe", // ArchUnit,
    "is",       // RestAssured,
    "spec",     // RestAssured,
    "verify"    // Mockito,
)

open class TbsRule(var language: Language = Language.JAVA) : Rule() {
    // todo: before visit check
    override fun visit(rootNode: Any, context: RuleContext, callback: IssueEmit) {
        this.visitRoot(rootNode as CodeDataStruct, context as TestSmellContext, callback)
    }

    private fun visitRoot(rootNode: CodeDataStruct, context: TestSmellContext, callback: IssueEmit) {
        rootNode.Annotations.forEachIndexed { index, it ->
            this.visitStructAnnotation(it, index, callback)
        }

        rootNode.Fields.forEachIndexed { index, it ->
            this.visitField(it, index, callback)
        }

        rootNode.Functions.forEachIndexed { index, it ->
            it.Annotations.forEachIndexed { annotationIndex, annotation ->
                this.visitFunctionAnnotation(it, annotation, annotationIndex, callback)
            }

            if (isTest(it)) {
                this.beforeVisitFunction(it, callback)

                this.visitFunction(it, index, callback)

                // in some cases, people would like to use assert in other function
                val currentMethodCalls =
                    addExtractAssertMethodCall(it, rootNode, context.methodMap)

                currentMethodCalls.forEachIndexed { callIndex, call ->
                    this.visitFunctionCall(it, call, callIndex, callback)
                }

                this.afterVisitFunction(it, callback)
            }
        }
    }

    protected fun isAssert(codeCall: CodeCall) =
        ASSERTION_LIST.contains(codeCall.FunctionName) || codeCall.FunctionName.startsWith("assert")

    // todo: condition by languages
    private fun isTest(it: CodeFunction): Boolean {
        return when (language) {
            Language.JAVA, Language.KOTLIN -> {
                val testsFilter = it.Annotations.filter { it.Name == "Test" || it.Name.endsWith(".Test") }
                testsFilter.isNotEmpty()
            }

            else -> {
                false
            }
        }
    }

    private fun addExtractAssertMethodCall(
        method: CodeFunction,
        node: CodeDataStruct,
        callMethodMap: MutableMap<String, CodeFunction>
    ): List<CodeCall> {
        val methodCalls = method.FunctionCalls.toMutableList()
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

    open fun visitField(field: CodeField, index: Int, callback: IssueEmit) {}

    open fun visitFunctionAnnotation(
        function: CodeFunction,
        annotation: CodeAnnotation,
        index: Int,
        callback: IssueEmit
    ) {
    }

    open fun beforeVisitFunction(function: CodeFunction, callback: IssueEmit) {}

    open fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {}

    open fun afterVisitFunction(function: CodeFunction, callback: IssueEmit) {}

    open fun visitFunctionCall(function: CodeFunction, codeCall: CodeCall, index: Int, callback: IssueEmit) {}

    open fun visitStructAnnotation(annotation: CodeAnnotation, index: Int, callback: IssueEmit) {}
}
