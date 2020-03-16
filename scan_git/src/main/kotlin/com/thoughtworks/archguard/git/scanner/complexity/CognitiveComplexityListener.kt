package com.thoughtworks.archguard.git.scanner.complexity

import dev.evolution.java.JavaParser
import dev.evolution.java.JavaParserBaseListener
import org.antlr.v4.runtime.ParserRuleContext

class CognitiveComplexityListener(val methods: MutableList<MethodCognitiveComplexity>) : JavaParserBaseListener() {

    private var nesting = 1
    private var complexity = 0


    override fun enterMethodDeclaration(ctx: JavaParser.MethodDeclarationContext?) {
        nesting = 1
        complexity = 0
        super.enterMethodDeclaration(ctx)
    }

    override fun exitMethodDeclaration(ctx: JavaParser.MethodDeclarationContext?) {
        val name = ctx!!.methodName().text
        val params = ctx.formalParameters().text
        methods.add(MethodCognitiveComplexity(name, params, complexity, buildPosition(ctx)))
        super.exitMethodDeclaration(ctx)
    }

    //start if
    override fun enterIfStatement(ctx: JavaParser.IfStatementContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterIfStatement(ctx)
    }

    override fun exitIfStatement(ctx: JavaParser.IfStatementContext?) {
        nesting--
        super.exitIfStatement(ctx)
    }

    override fun enterElseStatement(ctx: JavaParser.ElseStatementContext?) {
        //else if, else increase one nesting
        increaseComplexityByOne(ctx!!)
        if (!isElseIfStatement(ctx)) {
            nesting++
        }
        if (isElseIfStatement(ctx)) {
            complexity -= nesting
        }
        super.enterElseStatement(ctx)
    }

    override fun exitElseStatement(ctx: JavaParser.ElseStatementContext?) {
        if (!isElseIfStatement(ctx)) {
            nesting--
        }
        super.enterElseStatement(ctx)
    }

    private fun isElseIfStatement(ctx: JavaParser.ElseStatementContext?): Boolean {
        return ctx!!.statement().getChild(0) is JavaParser.IfStatementContext
    }
    //end if

    override fun enterSwithStatement(ctx: JavaParser.SwithStatementContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterSwithStatement(ctx)
    }

    override fun exitSwithStatement(ctx: JavaParser.SwithStatementContext?) {
        nesting--
        super.exitSwithStatement(ctx)
    }

    override fun enterForStatement(ctx: JavaParser.ForStatementContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterForStatement(ctx)
    }

    override fun exitForStatement(ctx: JavaParser.ForStatementContext?) {
        nesting--
        super.exitForStatement(ctx)
    }

    override fun enterWhileStatement(ctx: JavaParser.WhileStatementContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterWhileStatement(ctx)
    }

    override fun exitWhileStatement(ctx: JavaParser.WhileStatementContext?) {
        nesting--
        super.exitWhileStatement(ctx)
    }

    override fun enterDoWhileStatement(ctx: JavaParser.DoWhileStatementContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterDoWhileStatement(ctx)
    }

    override fun exitDoWhileStatement(ctx: JavaParser.DoWhileStatementContext?) {
        nesting--
        super.exitDoWhileStatement(ctx)
    }

    override fun enterCatchClause(ctx: JavaParser.CatchClauseContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterCatchClause(ctx)
    }

    override fun exitCatchClause(ctx: JavaParser.CatchClauseContext?) {
        nesting--
        super.exitCatchClause(ctx)
    }

    override fun enterLambdaExpression(ctx: JavaParser.LambdaExpressionContext?) {
        increaseComplexityByNesting(ctx!!)
        nesting++
        super.enterLambdaExpression(ctx)
    }

    override fun exitLambdaExpression(ctx: JavaParser.LambdaExpressionContext?) {
        nesting--
        super.exitLambdaExpression(ctx)
    }

    override fun enterBreakStatement(ctx: JavaParser.BreakStatementContext?) {
        increaseComplexityByOne(ctx!!)
        super.enterBreakStatement(ctx)
    }

    override fun enterContinueStatement(ctx: JavaParser.ContinueStatementContext?) {
        increaseComplexityByOne(ctx!!)
        super.enterContinueStatement(ctx)
    }

    override fun enterExpression(ctx: JavaParser.ExpressionContext?) {
        val bop = ctx!!.bop
        if (bop != null && ("&&".equals(bop.text) || "||".equals(bop.text))) {
            increaseComplexityByOne(ctx)
        }
        super.enterExpression(ctx)
    }

    private fun increaseComplexityByNesting(ctx: ParserRuleContext) {
        increaseComplexity(ctx, nesting)
    }

    private fun increaseComplexityByOne(ctx: ParserRuleContext) {
        increaseComplexity(ctx, 1)
    }

    private fun increaseComplexity(ctx: ParserRuleContext, increase: Int) {
        complexity += increase
    }

    private fun buildPosition(ctx: ParserRuleContext): Position {
        return Position(ctx.start.line, ctx.stop.line)
    }

}