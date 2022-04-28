package com.thoughtworks.archguard.git.scanner

import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {

    @BeforeEach
    internal fun setUp() {
        val localPath = File("./build/ddd")

        // if test in local, skip clone
        if (!File(localPath, ".git").isDirectory) {
            Git.cloneRepository()
                .setURI("https://github.com/archguard/ddd-monolithic-code-sample")
                .setDirectory(localPath)
                .call()
        }
    }

    @Test
    fun run() {
        val file = File("output.sql")
        Runner().main(arrayOf("--path=./build/ddd", "--branch=master"))
        assertTrue(file.exists())
    }
}