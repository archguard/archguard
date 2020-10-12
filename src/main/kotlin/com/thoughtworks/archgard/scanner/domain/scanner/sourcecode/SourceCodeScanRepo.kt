package com.thoughtworks.archgard.scanner.domain.scanner.sourcecode

import java.io.File

interface SourceCodeScanRepo {
    fun updateJClassLoc(sql: File)
}