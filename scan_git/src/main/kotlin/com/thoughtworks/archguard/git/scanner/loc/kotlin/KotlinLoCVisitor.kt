package com.thoughtworks.archguard.git.scanner.loc.kotlin

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import dev.evolution.KotlinParser
import dev.evolution.KotlinParser.ObjectDeclarationContext
import dev.evolution.KotlinParserBaseVisitor
import org.antlr.v4.runtime.ParserRuleContext

class KotlinLoCVisitor(private var locs: ArrayList<JClassLoC>, private val module: String?) : KotlinParserBaseVisitor<Any?>() {
    private var currentPkg: String? = null

    override fun visitClassDeclaration(ctx: KotlinParser.ClassDeclarationContext): Any? {
        val loc = getLoc(ctx, ctx.simpleIdentifier().text)
        locs.add(loc)
        return super.visitClassDeclaration(ctx)
    }

    override fun visitPackageHeader(ctx: KotlinParser.PackageHeaderContext): Any? {
        currentPkg = if (ctx.identifier() != null) {
            ctx.identifier().text
        } else {
            ctx.text
        }
        return super.visitPackageHeader(ctx)
    }

    override fun visitObjectDeclaration(ctx: ObjectDeclarationContext): Any? {
        val loc = getLoc(ctx, ctx.simpleIdentifier().text)
        locs.add(loc)
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
                methodName = it.Identifier()?.text.let { "anonymous_" + (0..100).random() }

            }
        }
        val className = getFunClass(ctx)
        locs.find { it.clz.equals(className) }?.addMethod(methodName, count)

        return super.visitFunctionDeclaration(ctx)
    }

    private fun getFunClass(ctx: KotlinParser.FunctionDeclarationContext): String? {
        var parent = ctx.parent
        while (!(parent is KotlinParser.ClassDeclarationContext)) {
            if (parent == null) {
                return null
            }
            parent = parent.parent
        }
        return parent.simpleIdentifier().text
    }

    private fun getLoc(ctx: ParserRuleContext, currentClz: String): JClassLoC {
        val startLine = ctx.start.line
        val stopLine = ctx.stop.line
        val count = stopLine - startLine + 1
        return JClassLoC(module, currentPkg, currentClz, count)
    }

}