package com.thoughtworks.archgard.scanner.domain.tools

import java.io.File

interface BadSmellReport {
    fun getBadSmellReport(): File?
}

interface JavaDependencyReport {
    fun getDependencyReport(): File?
}

interface GitReport {
    fun getGitReport(): File?
}

interface StyleReport {
    fun getStyleReport(): List<File>
}

interface TestBadSmellReport {
    fun getTestBadSmellReport(): File?
}