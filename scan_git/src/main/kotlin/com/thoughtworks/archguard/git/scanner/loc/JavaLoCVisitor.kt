package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import dev.evolution.java.JavaParser
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

    private fun getLoc(ctx: ParserRuleContext, currentClz: String) {
        val startLine = ctx.start.line
        val stopLine = ctx.stop.line
        val count = stopLine - startLine + 1
        loc.pkg = currentPkg
        loc.clz = currentClz
        loc.loc = count
    }

}