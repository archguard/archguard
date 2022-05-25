package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Deprecated("Deprecated features, will be removed in future")
@Service
class GitHotFileScanner(val gitHotFileRepo: ScannerGitHotFileRepo, val jClassRepository: JClassRepository) : Scanner {

    private val log = LoggerFactory.getLogger(GitHotFileScanner::class.java)

    override fun canScan(context: ScanContext): Boolean {
        return true
    }

    override fun getScannerName(): String {
        return "GitHotFile"
    }

    @Transactional
    override fun scan(context: ScanContext) {
        val hotFileReport = getHotFileReport(context)
        val gitHotFiles = getGitHotFIles(hotFileReport, context)
        gitHotFileRepo.save(gitHotFiles)
        log.info("Saved gitHotFiles, systemId={}", context.systemId)
    }

    private fun getGitHotFIles(hotFileReport: List<GitHotFileVO>, context: ScanContext): List<GitHotFile> {
        return hotFileReport.map {
            var jclassId: String? = null
            if (it.className() != null) jclassId = jClassRepository.findClassBy(context.systemId, it.className()!!, it.moduleName())?.id
            GitHotFile(context.systemId, context.repo, it.path, it.moduleName(), it.className(), it.modifiedCount, jclassId)
        }
    }

    fun getHotFileReport(context: ScanContext): List<GitHotFileVO> {
        val consumer = InMemoryConsumer()
        val gitHotFileScannerTool = GitHotFileScannerTool(context.workspace, context.branch, consumer)
        val gitHotFileModifiedCountMap = gitHotFileScannerTool.getGitHotFileModifiedCountMap()
        return gitHotFileModifiedCountMap.entries.map { GitHotFileVO(it.key, it.value) }
    }
}
