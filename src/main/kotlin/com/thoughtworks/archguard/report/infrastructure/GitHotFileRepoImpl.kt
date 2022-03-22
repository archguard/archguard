package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import com.thoughtworks.archguard.report.domain.githotfile.GitHotFileRepo
import com.thoughtworks.archguard.report.domain.githotfile.GitPathChangeCount
import org.springframework.stereotype.Repository

@Repository
class GitHotFileRepoImpl(val gitHotFileDao: GitHotFileDao) : GitHotFileRepo {

    override fun findBySystemId(systemId: Long): List<GitHotFile> {
        return gitHotFileDao.findBySystemId(systemId)
    }

    override fun findCountBySytemId(systemId: Long): List<GitPathChangeCount> {
        return gitHotFileDao.findCountBySystemId(systemId)
    }
}
