package com.thoughtworks.archguard.scanner.sourcecode

import chapi.app.analyser.*
import chapi.app.analyser.config.ChapiConfig
import chapi.domain.core.CodeDataStruct
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option

class Runner : CliktCommand(help = "scan git to sql") {
    private val path: String by option(help = "local path").default(".")
    private val systemId: String by option(help = "system id").default("3")
    private val language: String by option(help = "langauge: Java, Kotlin, TypeScript, CSharp, Python, Golang").default("Java")

    override fun run() {
        var dataStructs: Array<CodeDataStruct> = arrayOf()
        when (language.lowercase()) {
            "java" -> {
                dataStructs = JavaAnalyserApp().analysisNodeByPath(path)
            }
            "kotlin" -> {
                dataStructs = KotlinAnalyserApp().analysisNodeByPath(path)
            }
            "typescript", "ts", "js", "javascript" -> {
                dataStructs = TypeScriptAnalyserApp().analysisNodeByPath(path)
            }
            "csharp", "c#" -> {
                dataStructs = CSharpAnalyserApp().analysisNodeByPath(path)
            }
            "python" -> {
                dataStructs = PythonAnalyserApp(ChapiConfig()).analysisNodeByPath(path)
            }
            "go" -> {
                dataStructs = GoAnalyserApp(ChapiConfig()).analysisNodeByPath(path)
            }
        }
        toSql(dataStructs, systemId)
    }

    private fun toSql(dataStructs: Array<CodeDataStruct>, systemId: String) {
        val repo = ClassRepository(systemId)

        dataStructs.forEach { data ->
            repo.saveClassElement(data)
        }
        repo.close()
    }
}

fun main(args: Array<String>) = Runner().main(args)

