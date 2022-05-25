package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.GitChangeRepo
import com.thoughtworks.archguard.change.domain.GitHotFile
import com.thoughtworks.archguard.change.domain.GitPathChangeCount
import org.springframework.stereotype.Repository

@Repository
class GitChangeRepoImpl(val gitChangeDao: GitChangeDao) : GitChangeRepo {

    override fun findBySystemId(systemId: Long): List<GitHotFile> {
        return gitChangeDao.findBySystemId(systemId)
    }

    override fun findCountBySystemId(systemId: Long): List<GitPathChangeCount> {
        return gitChangeDao.findCountBySystemId(systemId)
    }

    override fun findUnstableFile(systemId: Long): List<GitPathChangeCount> {
        val topLines = gitChangeDao.getTopLinesFile(systemId)
        val topChanges = gitChangeDao.getTopChangesFile(systemId)

        return topLines.filter { line ->
            topChanges.filter {
                it.path == line.path
            }.isNotEmpty()
        }.toList()
    }

    override fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String> {
        return gitChangeDao.findChangesByRange(systemId, startTime, endTime)
    }
}
