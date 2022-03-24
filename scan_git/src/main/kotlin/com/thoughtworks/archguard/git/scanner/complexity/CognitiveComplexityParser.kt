package com.thoughtworks.archguard.git.scanner.complexity


import dev.evolution.JavaLexer
import dev.evolution.JavaParser
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.IOException
import java.nio.file.Paths

class CognitiveComplexityParser : ICognitiveComplexityParser {
    @Throws(IOException::class)
    override fun processFile(file: String): List<MethodCognitiveComplexity> {
        val path = Paths.get(file)
        val stream = CharStreams.fromPath(path)
        val tree = parse(stream)
        return walk(tree)
    }

    override fun processCode(code: String): List<MethodCognitiveComplexity> {
        val _code = StringBuilder()
        if (!code.trim().startsWith("class")) {
            _code.append("class A {")
            _code.append(code)
            _code.append("}")
        } else {
            _code.append(code)
        }
        val stream = CharStreams.fromString(_code.toString())
        val tree = parse(stream)
        val res = walk(tree)
        return res
    }

    private fun parse(stream: CharStream): ParseTree {
        val lexer = JavaLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = JavaParser(tokens)
        //temp remove logging such as 'unrecognize token'
        lexer.removeErrorListeners()
        parser.removeErrorListeners()
        return parser.compilationUnit()
    }

    private fun walk(tree: ParseTree): List<MethodCognitiveComplexity> {
        val walker = ParseTreeWalker()
        val methods = ArrayList<MethodCognitiveComplexity>()
        walker.walk(CognitiveComplexityListener(methods), tree)
        return methods.filter { it.complexity > 0 }
    }

}