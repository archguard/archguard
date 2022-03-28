package org.archguard.scanner.bytecode

import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class ByteCodeParserTest {
    @Test
    fun parseHelloWorld() {
        val resource = this.javaClass.classLoader.getResource("classes/HelloWorld.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals("org.archguard.demo.HelloWorld", ds.NodeName)
        assertEquals(2, ds.Functions.size)
        assertEquals("<init>", ds.Functions[0].Name)
        assertEquals("main", ds.Functions[1].Name)
    }
}