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

    override fun findUnstableFile(systemId: Long, size: Long): List<GitPathChangeCount> {
        val topLines = gitChangeDao.getTopLinesFile(systemId, size)
        val topChanges = gitChangeDao.getTopChangesFile(systemId, size)

        return topLines.filter { line ->
            topChanges.any {
                it.path == line.path
            }
        }.toList()
    }

    override fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String> {
        return gitChangeDao.findChangesByRange(systemId, startTime, endTime)
    }
}
