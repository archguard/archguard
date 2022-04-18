package org.archguard.scanner.context.impl

import org.archguard.scanner.context.Context
import org.archguard.scanner.context.ScannerSpec

// TODO package the command line arguments
class SourceCodeContext(
    override val language: ScannerSpec,
    override val features: List<ScannerSpec>,
    override val callbackUrl: String,
    val path: String,
    val systemId: String,
    val withoutStorage: Boolean,
) : Context
