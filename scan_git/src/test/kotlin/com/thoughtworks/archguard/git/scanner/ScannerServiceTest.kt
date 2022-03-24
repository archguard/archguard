package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled

internal class ScannerServiceTest {

    @Test
    @Disabled
    fun should_generate_sql_file_from_git() {
        val jGitAdapter = JGitAdapter(CognitiveComplexityParser(), language)
        val scannerService = ScannerService(jGitAdapter, Bean2Sql(), language)
        scannerService.git2SqlFile("..", "master", "0", "0", 1)
    }
}