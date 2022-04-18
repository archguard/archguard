package org.archguard.scanner.core.context

data class ScannerSpec(
    val identifier: String,
    val host: String,
    val version: String,
    val jar: String,
)

