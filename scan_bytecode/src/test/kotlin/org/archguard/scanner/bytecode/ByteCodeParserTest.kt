package org.archguard.scanner.bytecode

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class ByteCodeParserTest {
    @Test
    fun java_hello_world() {
        val resource = this.javaClass.classLoader.getResource("classes/HelloWorld.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals("org.archguard.demo.HelloWorld", ds.NodeName)
        assertEquals(2, ds.Functions.size)
        assertEquals("<init>", ds.Functions[0].Name)
        assertEquals("main", ds.Functions[1].Name)
    }

    @Test
    fun scala_hello_world() {
        val resource = this.javaClass.classLoader.getResource("scala/Hello.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals("Hello", ds.NodeName)
        assertEquals(1, ds.Functions.size)
        assertEquals("main", ds.Functions[0].Name)
    }

    @Test
    fun should_get_parent() {
        val resource = this.javaClass.classLoader.getResource("inheritance/Child.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals("com.example.demo.Parent", ds.Extend)
        assertEquals("com.example.demo.Interface", ds.Implements[0])
    }

    @Test
    fun should_support_annotation() {
        val resource = this.javaClass.classLoader.getResource("annotation/DemoApplication.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals(1, ds.Annotations.size)
    }
}