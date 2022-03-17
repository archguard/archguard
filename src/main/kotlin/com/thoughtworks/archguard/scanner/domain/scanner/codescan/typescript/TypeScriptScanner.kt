package com.thoughtworks.archguard.scanner.domain.scanner.codescan.typescript

import com.thoughtworks.archguard.scanner.domain.ScanContext
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import org.springframework.stereotype.Service

@Service
class TypeScriptScanner: Scanner {
    override fun getScannerName(): String {
        return "TypeScriptScanner"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language == "TypeScript"
    }

    override fun scan(context: ScanContext) {
        val coca = TypeScriptTool(context.workspace, context.systemId)
        coca.analyse()
    }
}