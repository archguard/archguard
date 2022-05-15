package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.springframework.stereotype.Service

@Service
class ScaScanner : Scanner {
    override fun getScannerName(): String = "ScaScanner"
    override fun canScan(context: ScanContext): Boolean = true
    override fun scan(context: ScanContext) =
        TODO("deprecated, replace with StranglerScannerExecutor.run(context, SCA)")
}
