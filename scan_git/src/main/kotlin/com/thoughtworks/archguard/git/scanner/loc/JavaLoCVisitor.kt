package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import dev.evolution.java.JavaParser
import dev.evolution.java.JavaParser.MethodDeclarationContext
import dev.evolution.java.JavaParserBaseVisitor
import org.antlr.v4.runtime.ParserRuleContext

class JavaLoCVisitor(private val loc: JClassLoC) : JavaParserBaseVisitor<Any?>() {
    private var currentPkg: String? = null
    override fun visitClassDeclaration(ctx: JavaParser.ClassDeclarationContext): Any? {
        getLoc(ctx, ctx.IDENTIFIER().text)
        return super.visitClassDeclaration(ctx)
    }

    override fun visitInterfaceDeclaration(ctx: JavaParser.InterfaceDeclarationContext): Any? {
        getLoc(ctx, ctx.IDENTIFIER().text)
        return super.visitInterfaceDeclaration(ctx)
    }

    override fun visitPackageDeclaration(ctx: JavaParser.PackageDeclarationContext): Any? {
        currentPkg = ctx.qualifiedName().text
        return super.visitPackageDeclaration(ctx)
    }

    override fun visitMethodDeclaration(ctx: MethodDeclarationContext?): Any? {
        if (ctx == null) {
            return null
        }
        val children = ctx.children
        var count = 0
        var methodName = ""
        children.forEach {
            if (it is JavaParser.MethodBodyContext) {
                count = (it.stop.line - it.start.line + 1)
            } else if (it is JavaParser.MethodNameContext) {
                methodName = it.IDENTIFIER().text
            }
        }
        loc.addMethod(methodName, count)

        return super.visitMethodDeclaration(ctx)
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