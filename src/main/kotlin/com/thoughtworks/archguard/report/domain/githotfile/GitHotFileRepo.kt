package com.thoughtworks.archguard.report.domain.githotfile

interface GitHotFileRepo {
    fun findBySystemId(systemId: Long) : List<GitHotFile>
    fun findCountBySystemId(systemId: Long) : List<GitPathChangeCount>
    fun findUnstableFile(systemId: Long) : List<GitPathChangeCount>
}