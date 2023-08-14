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
    // todo: not implemented yet
    ARCHITECTURE,
    ESTIMATE,
    OPENAPI
    ;

    companion object {
        fun fromString(type: String): AnalyserType {
            return values().first { it.name.lowercase() == type.lowercase() }
        }
    }
}

