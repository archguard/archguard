package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFileVO
import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFileRepo
import org.springframework.stereotype.Repository

@Repository
class GitHotFileRepoImpl(val gitHotFileDao: GitHotFileDao) : GitHotFileRepo {

    override fun save(gitHotFileVOS: List<GitHotFileVO>) {
        gitHotFileDao.deleteBySystemId(gitHotFileVOS[0].systemId)
        gitHotFileDao.saveAll(gitHotFileVOS)
    }
    
    override fun findBySystemId(systemId: Long): List<GitHotFileVO> {
        return gitHotFileDao.findBySystemId(systemId)
    }
}
