package com.thoughtworks.archguard.change.application

import com.thoughtworks.archguard.change.domain.model.GitHotFile
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount
import com.thoughtworks.archguard.change.domain.repository.GitChangeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GitChangeApplicationService(
    val gitChangeRepository: GitChangeRepository,
    @Value("\${scm_git_hot_file.modified_count_baseline}")
    val modifiedCountBaseline: Int,
) {
    fun getGitHotFilesBySystemId(systemId: Long): List<GitHotFile> {
        return gitChangeRepository.findBySystemId(systemId)
            .filter { (it.jclassId != null) && (it.modifiedCount >= modifiedCountBaseline) }
    }

    fun getPathChangeCount(systemId: Long): List<GitPathChangeCount> {
        return gitChangeRepository.findCountBySystemId(systemId)
    }

    fun getUnstableFile(systemId: Long, size: Long): List<GitPathChangeCount> {
        return gitChangeRepository.findUnstableFile(systemId, size)
    }

    fun getChangesByRange(systemId: Long, startTime: String, endTime: String): List<String> {
        return gitChangeRepository.findChangesByRange(systemId, startTime, endTime)
    }
}
