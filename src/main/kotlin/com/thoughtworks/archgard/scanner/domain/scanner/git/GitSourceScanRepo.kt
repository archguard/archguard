package com.thoughtworks.archgard.scanner.domain.scanner.git

import java.io.File

interface GitSourceScanRepo {
    fun cleanupCommitLog(systemId: Long)
    fun cleanupChangeEntry(systemId: Long)
    fun saveGitReport(sql: File)
}