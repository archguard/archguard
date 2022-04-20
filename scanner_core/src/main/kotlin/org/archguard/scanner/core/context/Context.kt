package org.archguard.scanner.core.context

// context of the scanner runtime, hold the data and the client
interface Context {
    val type: AnalyserType
    val client: ArchGuardClient
}

enum class AnalyserType {
    SOURCE_CODE,
    GIT,
    JACOCO,
    ;
}
