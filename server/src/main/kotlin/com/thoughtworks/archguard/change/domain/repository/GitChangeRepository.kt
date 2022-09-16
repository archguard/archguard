package com.thoughtworks.archguard.change.domain.repository

import com.thoughtworks.archguard.change.domain.model.GitHotFile
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount

interface GitChangeRepository {
    fun findBySystemId(systemId: Long): List<GitHotFile>
    fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
    fun findUnstableFile(systemId: Long, size: Long): List<GitPathChangeCount>
    fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String>
}
