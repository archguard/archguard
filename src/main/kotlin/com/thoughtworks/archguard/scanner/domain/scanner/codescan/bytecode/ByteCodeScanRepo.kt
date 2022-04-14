package com.thoughtworks.archguard.scanner.domain.scanner.codescan.bytecode

import java.io.File

interface ByteCodeScanRepo {
    fun updateJClassLoc(sql: File)
}
