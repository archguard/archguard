package com.thoughtworks.archguard.scanner.domain.scanner.diff

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.springframework.stereotype.Service

@Service
class DiffChangesScanner : Scanner {
    override fun getScannerName(): String = "DiffChangesScanner"
    override fun canScan(context: ScanContext): Boolean = false
    override fun scan(context: ScanContext) =
        TODO("deprecated, replace with StranglerScannerExecutor.run(context, DIFF_CHANGES)")
}
