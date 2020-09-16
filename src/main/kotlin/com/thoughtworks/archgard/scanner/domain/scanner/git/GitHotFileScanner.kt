package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitHotFileScannerTool
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.springframework.stereotype.Service

@Service
class GitHotFileScanner(val gitHotFileRepo: GitHotFileRepo, val jClassRepository: JClassRepository) : Scanner {
    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun getScannerName(): String {
        return "GitHotFile"
    }

    override fun scan(context: ScanContext) {
        val hotFileReport = getHotFileReport(context)
        val gitHotFiles = getGitHotFIles(hotFileReport, context)
        gitHotFileRepo.save(gitHotFiles)
    }

    private fun getGitHotFIles(hotFileReport: List<GitHotFileVO>, context: ScanContext): List<GitHotFile> {
        return hotFileReport.map {
            var jclassId: String? = null
            if (it.className() != null) jclassId = jClassRepository.findClassBy(context.systemId, it.className()!!, it.moduleName())?.id
            GitHotFile(context.systemId, context.repo, it.path, it.moduleName(), it.className(), it.modifiedCount, jclassId)
        }
    }

    fun getHotFileReport(context: ScanContext) : List<GitHotFileVO> {
        val gitHotFileScannerTool = GitHotFileScannerTool(context.workspace, "master")
        val gitHotFileModifiedCountMap = gitHotFileScannerTool.getGitHotFileModifiedCountMap()
        return gitHotFileModifiedCountMap.entries.map { GitHotFileVO(it.key, it.value) }
    }

}