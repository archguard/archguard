package com.thoughtworks.archguard.git.scanner.loc.kotlin

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import dev.evolution.kotlin.KotlinParser
import dev.evolution.kotlin.KotlinParser.ObjectDeclarationContext
import dev.evolution.kotlin.KotlinParserBaseVisitor
import org.antlr.v4.runtime.ParserRuleContext

class KotlinLoCVisitor(private val loc: JClassLoC) : KotlinParserBaseVisitor<Any?>() {
    private var currentPkg: String? = null
    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext): Any? {
        getLoc(ctx, ctx.simpleIdentifier().text)
        return super.visitClassDeclaration(ctx)
    }

    override fun visitPackageHeader(ctx: KotlinParser.PackageHeaderContext): Any? {
        currentPkg = ctx.identifier().text
        return super.visitPackageHeader(ctx)
    }

    override fun visitObjectDeclaration(ctx: ObjectDeclarationContext): Any? {
        getLoc(ctx, ctx.simpleIdentifier().text)
        return super.visitChildren(ctx)
    }

    override fun visitFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext?): Any? {
        if (ctx == null) {
            return null
        }
        val children = ctx.children
        var count = 0
        var methodName = ""
        children.forEach {
            if (it is KotlinParser.FunctionBodyContext) {
                count = (it.stop.line - it.start.line + 1)
            } else if (it is KotlinParser.SimpleIdentifierContext) {
                methodName = it.Identifier().text
            }
        }
        loc.addMethod(methodName, count)

        return super.visitFunctionDeclaration(ctx)
    }

    private fun getLoc(ctx: ParserRuleContext, currentClz: String) {
        val startLine = ctx.start.line
        val stopLine = ctx.stop.line
        val count = stopLine - startLine + 1
        loc.pkg = currentPkg
        loc.clz = currentClz
        loc.loc = count
    }

}