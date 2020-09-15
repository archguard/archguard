package com.thoughtworks.archgard.scanner.domain.scanner.git

interface GitHotFileRepo {
    fun save(gitHotFileReport: List<GitHotFile>)
    fun delete(systemId: Long)
    fun findBySystemId(systemId: Long) : List<GitHotFile>
}