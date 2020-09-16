package com.thoughtworks.archgard.scanner.domain.scanner.git

interface GitHotFileRepo {
    fun save(gitHotFiles: List<GitHotFile>)
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}