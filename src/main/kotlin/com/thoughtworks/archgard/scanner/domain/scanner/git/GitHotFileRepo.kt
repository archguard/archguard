package com.thoughtworks.archgard.scanner.domain.scanner.git

interface GitHotFileRepo {
    fun save(gitHotFiles: List<GitHotFile>)
    fun deleteBySystemId(systemId: Long)
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}