package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScaScanner(@Autowired val scaAnalyserRepo: ScaAnalyserRepo) : Scanner {

    private val log = LoggerFactory.getLogger(ScaScanner::class.java)

    override fun getScannerName(): String {
        return "ScaScanner"
    }

    override fun canScan(context: ScanContext): Boolean {
        return context.language != "jvm"
    }

    override fun scan(context: ScanContext) {
        log.info("start scan project dependencies source")
        val scaScannerTool = ScaScannerTool(context.workspace, context.systemId, context.language, context.logStream, context.scannerVersion)
        val gitReport = scaScannerTool.getScaReport()
        if (gitReport != null) {
            scaAnalyserRepo.cleanupSca(context.systemId)
            scaAnalyserRepo.saveSca(gitReport)
            log.info("finished sca scan")
        } else {
            log.warn("get null sca from scan")
        }
    }
}
