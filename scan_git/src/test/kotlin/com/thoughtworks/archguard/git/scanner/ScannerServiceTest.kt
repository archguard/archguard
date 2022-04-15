package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Test
import java.io.File

internal class ScannerServiceTest {

    @Test
    fun should_generate_sql_file_from_git() {
        val localPath = File("./build/ddd")

        // if test in local, skip clone
        if (!File(localPath, ".git").isDirectory) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(localPath)
                .call()
        }

        val jGitAdapter = JGitAdapter("java")
        val scannerService = ScannerService(jGitAdapter, Bean2Sql())
        scannerService.git2SqlFile("./build/ddd", "master", "0", "0", 1)
    }
}