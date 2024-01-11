package org.archguard.scanner.core.sourcecode

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodePosition
import java.io.File
import kotlin.streams.asStream
import kotlin.streams.toList

/**
 * The `LanguageSourceCodeAnalyser` interface is a Kotlin language class that provides functionality for analysing
 * source code written in various programming languages. It extends the `SourceCodeAnalyser` interface and defines
 * additional methods and functions specific to the Kotlin language.
 *
 *
 * Example usage:
 * ```kotlin
 * val analyser: LanguageSourceCodeAnalyser = KotlinAnalyser()
 * val codeDataStructs = analyser.analyse()
 * for (codeDataStruct in codeDataStructs) {
 *     println(codeDataStruct)
 * }
 * ```
 *
 * Note: This interface does not provide an implementation for the methods. It is meant to be implemented by
 * concrete classes that provide the actual functionality.
 */
interface LanguageSourceCodeAnalyser : SourceCodeAnalyser {
    override fun analyse(input: Any?): List<Any>? = analyse()

    fun analyse(): List<CodeDataStruct>

    fun getFilesByPath(path: String, predicate: (File) -> Boolean = { true }): List<File> {
        return File(path).walk().asStream().parallel()
            .filter { it.isFile }
            .filter { predicate(it) }
            .toList()
    }

    fun File.readContent(): String {
        val text = readText()
        // fix for Window issue
        if (text.startsWith("\uFEFF")) {
            return text.substring(1);
        }
        return text
    }

    /**
     * output example:
     * ```code
     * @{annotation}
     * function_name(param1, param2) -> return_TYPE {
     *    // function-call
     * }
     * ```
     */
    fun display(function: CodeFunction): String {
        val annotation = function.Annotations.joinToString("\n") {
            val keyValues = it.KeyValues.joinToString(", ") { keyValue -> "${keyValue.Key} = ${keyValue.Value}" }
            "@${it.Name}($keyValues)"
        }

        val params = function.Parameters.joinToString(", ") { "${it.TypeValue}: ${it.TypeType}" }
        val returnType = function.ReturnType
        val body = function.FunctionCalls.joinToString("\n") {
            "// ->" + it.Package + "." + it.NodeName + "." + it.FunctionName + "(" + it.Parameters.joinToString(", ") + ")"
        }

        return """
            $annotation
            ${function.Name}($params) -> $returnType {
                $body
            }
        """.trimIndent()
    }

    fun contentByPosition(lines: List<String>, position: CodePosition): String {
        val startLine = if (position.StartLine == 0) {
            0
        } else {
            position.StartLine - 1
        }
        val endLine = if (position.StopLine == 0) {
            0
        } else {
            position.StopLine - 1
        }

        val startLineContent = lines[startLine]
        val endLineContent = lines[endLine]

        val startColumn = if (position.StartLinePosition > startLineContent.length) {
            if (startLineContent.isBlank()) {
                0
            } else {
                startLineContent.length - 1
            }
        } else {
            position.StartLinePosition
        }

        val endColumn = if (position.StopLinePosition > endLineContent.length) {
            if (endLineContent.isBlank()) {
                0
            } else {
                endLineContent.length - 1
            }
        } else {
            position.StopLinePosition
        }

        val start = startLineContent.substring(startColumn)
        val end = endLineContent.substring(0, endColumn)

        // start + ... + end
        return if (startLine == endLine) {
            start
        } else {
            start + lines.subList(startLine + 1, endLine).joinToString("") + end
        }
    }
}

