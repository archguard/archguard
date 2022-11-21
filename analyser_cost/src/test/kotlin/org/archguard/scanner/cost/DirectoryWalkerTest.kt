package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import org.archguard.scanner.cost.count.FileJob
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class DirectoryWalkerTest {
    @Test
    fun testWalk() {
        val rootDir = Paths.get("").toAbsolutePath().parent
        val channel = Channel<FileJob>()
        val walker = DirectoryWalker(channel).walk(rootDir.toString())!!

        walker.forEach {
            println(it.absolutePath)
        }
    }
}