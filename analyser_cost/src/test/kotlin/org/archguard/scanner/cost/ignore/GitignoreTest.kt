package org.archguard.scanner.cost.ignore

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

internal class GitignoreTest {
    @Test
    fun testNewGitIgnore() {
        val basepath = this.javaClass.classLoader.getResource("ignore/should_include")!!.path
        val path = this.javaClass.classLoader.getResource("ignore/.ignoretest")!!.path

        val ignore = Gitignore(path)
        assertFalse(ignore.match(basepath, false))
    }

    @Test
    fun testProjectGitignore() {
        val rootDir = Paths.get("").toAbsolutePath().parent

        val gitignore = rootDir.resolve(".gitignore")
        val ignore = Gitignore(gitignore.toString())

        assertFalse(ignore.match(rootDir.resolve("build").toString(), false))
        assertFalse(ignore.match(rootDir.resolve("analyser_cost").toString(), false))
    }
}