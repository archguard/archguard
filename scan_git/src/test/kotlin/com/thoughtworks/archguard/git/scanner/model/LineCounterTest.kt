package com.thoughtworks.archguard.git.scanner.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class LineCounterTest {
    @Test
    internal fun testForGbkFile() {
        val resource = this.javaClass.classLoader.getResource("gbkfiles/hello.go")!!
        val path = Paths.get(resource.toURI()).toFile().absolutePath

        assertEquals(9, LineCounter.byPath(path))
    }
}