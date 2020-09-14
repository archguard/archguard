package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitHotFileScannerTool
import org.springframework.stereotype.Service

@Service
class GitHotFileScanner() : Scanner {
    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun getScannerName(): String {
        return "GitHotFile"
    }

    override fun scan(context: ScanContext) {
        val gitHotFileScannerTool = GitHotFileScannerTool(context.workspace, "master")
        val gitHotFileReport = gitHotFileScannerTool.getGitHotFileReport()
    }

}