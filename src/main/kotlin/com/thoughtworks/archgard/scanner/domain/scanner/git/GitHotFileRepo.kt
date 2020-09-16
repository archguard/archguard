package com.thoughtworks.archgard.scanner.domain.scanner.git

interface GitHotFileRepo {
    fun save(gitHotFileVOS: List<GitHotFileVO>)
    fun findBySystemId(systemId: Long) : List<GitHotFileVO>
}