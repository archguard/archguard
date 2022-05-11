package org.archguard.scanner.core.context

import org.archguard.scanner.core.client.ArchGuardClient

// context of the scanner runtime, hold the data and the client
interface Context {
    val type: AnalyserType
    val client: ArchGuardClient
}

enum class AnalyserType {
    SOURCE_CODE,
    GIT,
    DIFF_CHANGES,
    SCA,
    RULE,
    ;
}

