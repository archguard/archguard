package org.archguard.scanner.core.context

import org.archguard.scanner.core.client.ArchGuardClient

// context of the scanner runtime, hold the data and the api
interface Context {
    val type: AnalyserType
    val client: ArchGuardClient
    // TODO: add slot
//    val slotHub: SlotHub
}

enum class AnalyserType {
    SOURCE_CODE,
    GIT,
    DIFF_CHANGES,
    SCA,
    RULE,
    ARCHITECTURE,
    ESTIMATE,
    OPENAPI,
    DOCUMENT
    ;

    companion object {
        fun fromString(type: String): AnalyserType? {
            return values().firstOrNull() { it.name.lowercase() == type.lowercase() }
        }
    }
}

