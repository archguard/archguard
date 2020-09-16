package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import com.thoughtworks.archguard.report.domain.githotfile.GitHotFileRepo
import org.springframework.stereotype.Service

@Service
class GitHotFileService(val gitHotFileRepo: GitHotFileRepo) {
    fun getGitHotFilesBySystemId(systemId: Long) : List<GitHotFile> {
        return gitHotFileRepo.findBySystemId(systemId).filter { (it.jclassId != null) && (it.modifiedCount >= MODIFIED_COUNT_BASELINE) }
    }

    companion object {
        const val MODIFIED_COUNT_BASELINE = 10
    }

}
