package org.archguard.scanner.core.sourcecode

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodePosition
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.streams.asStream
import kotlin.streams.toList

// 重载函数, 实现细粒度的接口定义
interface LanguageSourceCodeAnalyser : SourceCodeAnalyser {
    companion object {
        private val logger = LoggerFactory.getLogger(LanguageSourceCodeAnalyser::class.java)
    }

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

