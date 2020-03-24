package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitScannerTool
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GitSourceScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {

    override fun scan(context: ScanContext) {
        val gitScannerTool = GitScannerTool(context.workspace, "master")
        val gitReport = gitScannerTool.getGitReport()
        if (gitReport != null) {
            sqlScriptRunner.run(gitReport)
        }
    }

}