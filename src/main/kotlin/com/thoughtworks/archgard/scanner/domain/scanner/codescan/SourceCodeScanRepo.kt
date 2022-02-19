package com.thoughtworks.archgard.scanner.domain.scanner.codescan

import java.io.File

interface SourceCodeScanRepo {
    fun updateJClassLoc(sql: File)
}