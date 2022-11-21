package org.archguard.scanner.cost

import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class DirectoryWalkerTest {
    @Test
    fun testWalk() {
        val rootDir = Paths.get("").toAbsolutePath().parent
        val walker = DirectoryWalker.walk(rootDir.toString())!!
        walker.forEach { println(it) }
    }
}