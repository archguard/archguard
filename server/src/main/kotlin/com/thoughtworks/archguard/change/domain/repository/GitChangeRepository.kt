package com.thoughtworks.archguard.change.domain.repository

import com.thoughtworks.archguard.change.domain.model.GitHotFilePO
import com.thoughtworks.archguard.change.domain.model.GitPathChangeCount

interface GitChangeRepository {
    fun findBySystemId(systemId: Long): List<GitHotFilePO>
    fun findCountBySystemId(systemId: Long): List<GitPathChangeCount>
    fun findUnstableFile(systemId: Long, size: Long): List<GitPathChangeCount>
    fun findChangesByRange(systemId: Long, startTime: String, endTime: String): List<String>
}
