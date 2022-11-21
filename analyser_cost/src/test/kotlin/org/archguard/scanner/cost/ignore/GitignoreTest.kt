package org.archguard.scanner.cost.ignore

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class GitignoreTest {
    @Test
    @Disabled
    fun testNewGitIgnore() {
        val basepath = this.javaClass.classLoader.getResource("ignore")!!.path
        val path = this.javaClass.classLoader.getResource("ignore/.ignoretest")!!.path

        val ignore = Gitignore(path, basepath)
        assertFalse(ignore.match(basepath, false))
    }
}