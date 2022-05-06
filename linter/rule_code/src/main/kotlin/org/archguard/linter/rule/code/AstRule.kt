package org.archguard.linter.rule.code

import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import org.archguard.rule.common.Language

open class AstRule(var language: Language = Language.JAVA) : Rule() {
    override fun visit(rootNode: Any, context: RuleContext, callback: IssueEmit) {
        visitRoot(rootNode as CodeDataStruct, callback)
    }

    private fun visitRoot(rootNode: CodeDataStruct, callback: IssueEmit) {
        this.visitPackage(rootNode.Package, callback)

        rootNode.Fields.forEachIndexed { index, it ->
            this.visitField(it, index, callback)
        }

        rootNode.Functions.forEachIndexed { index, it ->
            it.Annotations.forEachIndexed { annotationIndex, annotation ->
                this.visitFunctionAnnotation(it, annotation, annotationIndex, callback)
            }

            this.beforeVisitFunction(it, callback)

            this.visitFunction(it, index, callback)

            this.afterVisitFunction(it, callback)
        }
    }

    open fun visitField(field: CodeField, index: Int, callback: IssueEmit) {}

    open fun visitPackage(packageName: String, callback: IssueEmit) {}

    open fun visitFunctionAnnotation(function: CodeFunction, annotation: CodeAnnotation, index: Int, callback: IssueEmit) {}

    open fun beforeVisitFunction(function: CodeFunction, callback: IssueEmit) {}

    open fun visitFunction(function: CodeFunction, index: Int, callback: IssueEmit) {}

    open fun afterVisitFunction(function: CodeFunction, callback: IssueEmit) {}
}