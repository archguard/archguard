package com.thoughtworks.archguard.smartscanner

data class ScannerCommand(
    val type: AnalyserType,
    val systemId: String,
    val serverUrl: String,
    val path: String,
) {
    // for source code analysing, may be Map, String or AnalyserSpec
    var language: Any? = null
    var features: List<Any> = listOf()
}

data class AnalyserSpec(
    val identifier: String,
    val host: String,
    val version: String,
    val jar: String,
    val className: String,
)

enum class AnalyserType {
    SOURCE_CODE,
    GIT,
    JACOCO,
    ;
}
