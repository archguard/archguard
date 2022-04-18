package org.archguard.scanner.context

data class ScannerSpec(
    val identifier: String,
    val host: String,
    val version: String,
    val jar: String,
)

