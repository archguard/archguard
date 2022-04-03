package com.thoughtworks.archguard.change.domain

interface GitHotFileRepo {
    fun findBySystemId(systemId: Long) : List<GitHotFile>
    fun findCountBySystemId(systemId: Long) : List<GitPathChangeCount>
    fun findUnstableFile(systemId: Long) : List<GitPathChangeCount>
}