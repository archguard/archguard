package org.archguard.scanner.core

import kotlinx.serialization.Serializable

@Serializable
data class AnalyserSpec(
    val identifier: String,
    val host: String,
    val version: String,
    val jar: String,
    val className: String, // calculate via identifier??
    // additional type for slot
    val slotType: String = "",
)

typealias SlotSpec = AnalyserSpec;
