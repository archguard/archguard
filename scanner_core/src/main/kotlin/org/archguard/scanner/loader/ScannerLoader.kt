package org.archguard.scanner.loader

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.context.Context
import org.archguard.scanner.context.ScannerSpec
import org.archguard.scanner.context.impl.OfficialScannerSpecs
import org.archguard.scanner.context.impl.SourceCodeContext
import org.archguard.scanner.core.LanguageScanner
import org.archguard.scanner.core.Scanner
import org.archguard.scanner.core.ScannerExecutor

object ScannerLoader {
    fun load(context: Context, spec: ScannerSpec): Scanner<Context> {
        // isInstalled
        // install
        // get with class for name
        TODO("Not yet implemented")
    }
}

fun main() {
    // get the inputs from clikt command
    // create the context by cli parameters
    val context: Context = SourceCodeContext(
        language = OfficialScannerSpecs.JAVA.spec,
        features = emptyList(),
        path = "src/main/kotlin/org/archguard/rule/core/ScannerVisitor.kt",
        systemId = "empty",
        withoutStorage = false,
        callbackUrl = "string",
    )
    // execute
    ScannerExecutor(context).execute()
}

class JavaScanner(override val context: Context) : LanguageScanner<SourceCodeContext> {
    override fun execute(): List<CodeDataStruct> {
        return emptyList()
    }
}
