package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.GitHotFile
import com.thoughtworks.archguard.change.domain.GitHotFileRepo
import com.thoughtworks.archguard.change.domain.GitPathChangeCount
import org.springframework.stereotype.Repository

@Repository
class GitHotFileRepoImpl(val gitHotFileDao: GitHotFileDao) : GitHotFileRepo {

    override fun findBySystemId(systemId: Long): List<GitHotFile> {
        return gitHotFileDao.findBySystemId(systemId)
    }

    override fun findCountBySystemId(systemId: Long): List<GitPathChangeCount> {
        return gitHotFileDao.findCountBySystemId(systemId)
    }

    override fun findUnstableFile(systemId: Long): List<GitPathChangeCount> {
        val topLines = gitHotFileDao.getTopLinesFile(systemId)
        val topChanges = gitHotFileDao.getTopChangesFile(systemId)

        return topLines.filter { line ->
            topChanges.filter {
                it.path == line.path
            }.isNotEmpty()
        }.toList()
    }
}
