package com.thoughtworks.archguard.scanner.domain.scanner.git

interface ScannerGitHotFileRepo {
    fun save(gitHotFiles: List<GitHotFile>)
}
