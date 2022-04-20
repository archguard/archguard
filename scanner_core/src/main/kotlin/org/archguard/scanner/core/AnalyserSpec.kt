package org.archguard.scanner.core

data class AnalyserSpec(
    val identifier: String,
    val host: String,
    val version: String,
    val jar: String,
    val className: String, // calculate via identifier??
)

