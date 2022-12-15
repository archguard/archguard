package org.archguard.scanner.estimate

import io.kotest.matchers.comparables.shouldBeGreaterThan
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.estimate.count.FileJob
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong

internal class DirectoryWalkerTest {
    @Test
    fun testWalk() {
        val rootDir = Paths.get("").toAbsolutePath()
        val count = AtomicLong(0)
        runBlocking {
            val channel = Channel<FileJob>()
            launch {
                for (fileJob in channel) {
                    count.incrementAndGet()
                }
            }

            val walker = DirectoryWalker(channel)
            walker.start(rootDir.toString())

            channel.close()
        }

        println(count.get())
        count.get() shouldBeGreaterThan 10
    }
}