package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.cost.count.FileJob
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class DirectoryWalkerTest {
    @Test
    fun testWalk() {
        val rootDir = Paths.get("").toAbsolutePath().parent
        runBlocking {
            val channel = Channel<FileJob>()
            launch {
                for (fileJob in channel) {
                    println(fileJob.location)
                }
            }

            val walker = DirectoryWalker(channel)
            walker.walk(rootDir.toString())
            channel.close()
        }
    }
}