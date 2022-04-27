package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sourcecode

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.springframework.stereotype.Service

@Service
class SourceCodeScanner : Scanner {
    override fun getScannerName(): String {
        return "SourceCodeScanner"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language.lowercase() != "jvm"
    }

    override fun scan(context: ScanContext) {
        SourceCodeTool(
            context.workspace,
            context.systemId,
            context.language,
            context.dbUrl,
            context.codePath,
            context.logStream,
            context.scannerVersion
        ).also {
            it.analyse()
        }
    }
}
