package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca

import java.io.File

interface ScaAnalyserRepo {
    fun cleanupSca(systemId: Long)
    fun saveSca(sql: File)
}
