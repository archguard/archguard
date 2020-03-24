package com.thoughtworks.archgard.scanner.domain.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import com.thoughtworks.archgard.scanner.domain.toolscanners.GitScanner
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GitSourceScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {

    override fun scan(context: ScanContext) {
        val gitScanner = GitScanner(context.workspace, "master")
        val gitReport = gitScanner.getGitReport()
        if (gitReport != null) {
            sqlScriptRunner.run(gitReport)
        }
    }

}