package com.thoughtworks.archguard.scanner.domain.tools

import java.io.File

interface StyleReport {
    fun getStyleReport(): List<File>
}

interface TestBadSmellReport {
    fun getTestBadSmellReport(): File?
}
