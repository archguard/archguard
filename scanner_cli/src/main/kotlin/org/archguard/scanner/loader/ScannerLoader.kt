package org.archguard.scanner.loader

import org.archguard.scanner.core.Scanner
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.context.ScannerSpec

// TODO load the scanner via classloader
object ScannerLoader {
    fun load(context: Context, spec: ScannerSpec): Scanner<Context> {
        // isInstalled
        // install
        // get with class for name
        TODO("Not yet implemented")
    }
}
