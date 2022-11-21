package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.cost.count.FileJob
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class DirectoryWalkerTest {
    @Test
    @Disabled
    fun testWalk() {
        val rootDir = Paths.get("").toAbsolutePath().parent
        runBlocking {
            val channel = Channel<FileJob>()
            launch {
                for (fileJob in channel) {
                    println("received: ${fileJob.location}")
                }
            }

            val walker = DirectoryWalker(channel)
            walker.start(rootDir.toString())

            channel.close()
        }
    }
}