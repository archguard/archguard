package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.git.GitHotFile
import com.thoughtworks.archguard.scanner.domain.scanner.git.ScannerGitHotFileRepo
import org.springframework.stereotype.Repository

@Repository
class ScannerGitHotFileRepoImpl(val scannerGitHotFileDao: ScannerGitHotFileDao) : ScannerGitHotFileRepo {

    // FIXME
    override fun save(gitHotFiles: List<GitHotFile>) {
        scannerGitHotFileDao.deleteBySystemId(gitHotFiles[0].systemId)
        scannerGitHotFileDao.saveAll(gitHotFiles)
    }

    override fun findBySystemId(systemId: Long): List<GitHotFile> {
        return scannerGitHotFileDao.findBySystemId(systemId)
    }
}
