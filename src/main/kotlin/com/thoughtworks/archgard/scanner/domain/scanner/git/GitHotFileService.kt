package com.thoughtworks.archgard.scanner.domain.scanner.git

import org.springframework.stereotype.Service

@Service
class GitHotFileService(val gitHotFileRepo: GitHotFileRepo) {
    fun getGitHotFilesBySystemId(systemId: Long) : List<GitHotFile> {
        return gitHotFileRepo.findBySystemId(systemId)
    }

}
