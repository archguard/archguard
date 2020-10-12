package com.thoughtworks.archguard.git.scanner.loc

import dev.evolution.java.JavaLexer
import dev.evolution.java.JavaParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.io.IOException
import java.nio.file.Path

object JavaFileParser {
    fun parse(path: Path?): ParseTree {
        val stream = CharStreams.fromPath(path)
        val lexer = JavaLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = JavaParser(tokens)
        return parser.compilationUnit()
    }
}