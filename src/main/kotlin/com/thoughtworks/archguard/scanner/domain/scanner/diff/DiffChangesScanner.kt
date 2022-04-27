package com.thoughtworks.archguard.scanner.domain.scanner.diff

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.springframework.stereotype.Service

@Service
class DiffChangesScanner : Scanner {
    override fun getScannerName(): String {
        return "DiffChangesScanner"
    }

    override fun canScan(context: ScanContext): Boolean {
        return false
    }

    override fun scan(context: ScanContext) {
        DiffChangesTool(
            context.workspace,
            context.systemId,
            context.language,
            context.dbUrl,
            context.branch,
            context.logStream,
            context.additionArguments,
            context.scannerVersion
        ).also {
            it.analyse()
        }
    }
}
