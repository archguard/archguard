package com.thoughtworks.archguard.scanner.domain.scanner.codescan

import java.io.File

interface SourceCodeScanRepo {
    fun updateJClassLoc(sql: File)
}