package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import com.thoughtworks.archguard.report.domain.githotfile.GitHotFileRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GitFileService(val gitHotFileRepo: GitHotFileRepo, @Value("\${git_hot_file.modified_count_baseline}") val modifiedCountBaseline: Int) {
    fun getGitHotFilesBySystemId(systemId: Long) : List<GitHotFile> {
        return gitHotFileRepo.findBySystemId(systemId)
            .filter { (it.jclassId != null) && (it.modifiedCount >= modifiedCountBaseline) }
    }

    fun getGitFileChanges(systemId: Long) : List<GitHotFile> {
        return gitHotFileRepo.findBySystemId(systemId)
    }
}
