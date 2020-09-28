package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitScannerTool
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GitSourceScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {
    private val DELETE_COMMIT_LOG = "delete from commit_log where system_id="
    private val DELETE_CHANGE_ENTRY = "delete from change_entry system_id="

    private val log = LoggerFactory.getLogger(GitSourceScanner::class.java)
    override fun getScannerName(): String {
        return "GitSource"
    }

    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun scan(context: ScanContext) {
        log.info("start scan git source")
        val gitScannerTool = GitScannerTool(context.workspace, "master", context.systemId, context.repo)
        val gitReport = gitScannerTool.getGitReport()
        if (gitReport != null) {
            sqlScriptRunner.run(DELETE_COMMIT_LOG + context.systemId)
            sqlScriptRunner.run(DELETE_CHANGE_ENTRY + context.systemId)
            sqlScriptRunner.run(gitReport)
        }
        log.info("finished scan git source")
    }

}