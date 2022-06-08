package com.thoughtworks.archguard.change.domain

interface GitChangeRepo {
    fun findBySystemId(systemId: Long): List<GitHotFile>
    fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
    fun findUnstableFile(systemId: Long, size: Long): List<GitPathChangeCount>
    fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String>
}
