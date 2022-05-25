package org.archguard.scanner.core.sourcecode

import chapi.domain.core.CodeDataStruct
import java.io.File
import kotlin.streams.asStream
import kotlin.streams.toList

// 重载函数, 实现细粒度的接口定义
interface LanguageSourceCodeAnalyser : SourceCodeAnalyser {
    override fun analyse(input: Any?): Any? = analyse()

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
}
