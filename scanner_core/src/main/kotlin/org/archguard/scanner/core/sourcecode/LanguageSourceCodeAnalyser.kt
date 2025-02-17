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
     * This method generates a formatted display of a given Kotlin language function.
     *
     * @param function The CodeFunction object representing the function to be displayed.
     * @return A string containing the formatted display of the function.
     *
     * Example usage:
     * ```kotlin
     * val function = CodeFunction()
     * // Set function properties
     * val display = display(function)
     * println(display)
     * ```
     *
     * The output will be:
     * ```
     * @{annotation}
     * function_name(param1: Type1, param2: Type2) -> return_TYPE {
     *    // function-call
     * }
     * ```
     *
     * The `display` method takes a CodeFunction object as input and generates a formatted display of the function.
     * It starts by extracting the annotations of the function and concatenating them into a single string,
     * with each annotation formatted as `@annotationName(key1 = value1, key2 = value2)`. This string is stored in
     * the `annotation` variable.
     */
    fun display(function: CodeFunction): String {
        val annotation = function.Annotations.joinToString("\n") {
            val keyValues = it.KeyValues.joinToString(", ") { keyValue -> "${keyValue.Key} = ${keyValue.Value}" }
            "@${it.Name}($keyValues)"
        }

        val params = function.Parameters.joinToString(", ") { "${it.TypeValue}: ${it.TypeType}" }
        val returnType = function.ReturnType
        val body = function.FunctionCalls.joinToString("\n") {
            val parameters = it.Parameters.joinToString(", ") { "${it.TypeValue}: ${it.TypeType}" }
            "// ->" + it.Package + "." + it.NodeName + "." + it.FunctionName + "(" + parameters + ")"
        }

        return """
            $annotation
            ${function.Name}($params) -> $returnType {
                $body
            }
        """.trimIndent()
    }

    /**
     * Returns the content of a code snippet specified by the given position in a list of lines.
     *
     * @param lines The list of lines containing the code snippet.
     * @param position The position of the code snippet.
     * @return The content of the code snippet specified by the position.
     */
    fun contentByPosition(lines: List<String>, position: CodePosition): String {
        // Convert one-based indices to zero-based, with a guard for 0.
        val startLine: Int = if (position.StartLine == 0) 0 else position.StartLine - 1
        val endLine: Int = if (position.StopLine == 0) 0 else position.StopLine - 1

        // Safely get the start and end line contents.
        val startLineContent: String = lines.getOrElse(startLine) { "" }
        val endLineContent: String = lines.getOrElse(endLine) { "" }

        // Determine the effective start column.
        val startColumn: Int = if (position.StartLinePosition > startLineContent.length) {
            if (startLineContent.isBlank()) 0 else startLineContent.length - 1
        } else {
            position.StartLinePosition
        }

        // Determine the effective end column.
        val endColumn: Int = if (position.StopLinePosition > endLineContent.length) {
            if (endLineContent.isBlank()) 0 else endLineContent.length - 1
        } else {
            position.StopLinePosition
        }

        // Extract the substring from the start line.
        val start: String = startLineContent.substring(startColumn)
        // Extract the substring from the end line.
        val end: String = endLineContent.substring(0, endColumn)

        // Single-line case.
        return if (startLine == endLine) {
            start
        } else {
            // Rejoin middle lines with "\n" to preserve newline formatting.
            start + "\n" + lines.subList(startLine + 1, endLine).joinToString(separator = "\n") + "\n" + end
        }
    }
}

// 