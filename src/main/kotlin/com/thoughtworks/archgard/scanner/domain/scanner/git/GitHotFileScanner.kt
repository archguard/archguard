package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitHotFileScannerTool
import org.springframework.stereotype.Service

@Service
class GitHotFileScanner(val gitHotFileRepo: GitHotFileRepo) : Scanner {
    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun getScannerName(): String {
        return "GitHotFile"
    }

    override fun scan(context: ScanContext) {
        val hotFileReport = getHotFileReport(context)
        gitHotFileRepo.save(hotFileReport.map { GitHotFile(context.systemId, context.repo, it.name, it.moduleName(), it.className(), it.modifiedCount,null) })
    }

    fun getHotFileReport(context: ScanContext) : List<GitHotFileVO> {
        val gitHotFileScannerTool = GitHotFileScannerTool(context.workspace, "master")
        val gitHotFileModifiedCountMap = gitHotFileScannerTool.getGitHotFileModifiedCountMap()
        return gitHotFileModifiedCountMap.entries.map { GitHotFileVO(it.key, it.value) }
    }

}