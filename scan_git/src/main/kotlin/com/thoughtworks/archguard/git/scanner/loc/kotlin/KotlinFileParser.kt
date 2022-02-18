package com.thoughtworks.archguard.git.scanner.loc.kotlin

import dev.evolution.KotlinLexer
import dev.evolution.KotlinParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path

object KotlinFileParser {
    fun parse(path: Path?): ParseTree {
        val stream = CharStreams.fromPath(path)
        val lexer = KotlinLexer(stream)
        val tokens = CommonTokenStream(lexer)
        val parser = KotlinParser(tokens)
        return parser.kotlinFile()
    }
}