package com.thoughtworks.archgard.scanner.domain.toolscanners

interface BadSmellReport {
    fun getBadSmellReport(): String
}

interface JavaDependencyReport {
    fun getDependencyReport()
}

interface TestBadSmellReport {
    fun getTestBadSmellReport(): String
}