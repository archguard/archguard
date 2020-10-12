package com.thoughtworks.archgard.scanner.domain.scanner.sourcecode

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitScannerTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SourceCodeScanner(@Autowired val sourceCodeScanRepo: SourceCodeScanRepo) : Scanner {

    private val log = LoggerFactory.getLogger(SourceCodeScanner::class.java)

    override fun getScannerName(): String {
        return "GitSource"
    }

    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun scan(context: ScanContext) {
        log.info("start update loc")
        val gitScannerTool = GitScannerTool(context.workspace, null, context.systemId, context.repo)
        val locReport = gitScannerTool.getLocReport()
        if (locReport != null) {
            sourceCodeScanRepo.updateJClassLoc(locReport)
            log.info("finished scan loc source")
        } else {
            log.warn("failed to scan loc")
        }
    }

}