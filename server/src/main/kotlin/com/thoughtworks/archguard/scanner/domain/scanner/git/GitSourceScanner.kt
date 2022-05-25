package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.springframework.stereotype.Service

@Service
class GitSourceScanner : Scanner {
    override fun getScannerName(): String = "GitSourceScanner"
    override fun canScan(context: ScanContext): Boolean = true
    override fun scan(context: ScanContext) =
        TODO("deprecated, replace with StranglerScannerExecutor.run(context, GIT)")
}
