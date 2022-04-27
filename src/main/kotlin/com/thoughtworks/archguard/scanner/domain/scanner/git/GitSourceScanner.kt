package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GitSourceScanner(@Autowired val gitSourceScanRepo: GitSourceScanRepo) : Scanner {

    private val log = LoggerFactory.getLogger(GitSourceScanner::class.java)

    override fun getScannerName(): String {
        return "GitSource"
    }

    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun scan(context: ScanContext) {
        log.info("start scan git source")
        val gitScannerTool = GitScannerTool(context.workspace, context.branch, context.systemId, context.repo, context.logStream, context.scannerVersion)
        val gitReport = gitScannerTool.getGitReport()
        if (gitReport != null) {
            gitSourceScanRepo.cleanupCommitLog(context.systemId)
            gitSourceScanRepo.cleanupChangeEntry(context.systemId)
            gitSourceScanRepo.saveGitReport(gitReport)
            log.info("finished scan git source")
        } else {
            log.warn("get null git report from scan")
        }
    }
}
