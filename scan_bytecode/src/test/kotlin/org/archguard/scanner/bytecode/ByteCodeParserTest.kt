package org.archguard.scanner.bytecode

import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertEquals

internal class ByteCodeParserTest {
    @Test
    fun java_hello_world() {
        val resource = this.javaClass.classLoader.getResource("classes/HelloWorld.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals("HelloWorld", ds.NodeName)
        assertEquals("org.archguard.demo", ds.Package)
        assertEquals(2, ds.Functions.size)
        assertEquals("<init>", ds.Functions[0].Name)
        assert(ds.Functions[0].IsConstructor)
        assertEquals("main", ds.Functions[1].Name)
        assertEquals("void", ds.Functions[1].ReturnType)
    }

    @Test
    fun should_support_for_modifiers() {
        val resource = this.javaClass.classLoader.getResource("classes/HelloWorld.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        val modifiers = ds.Functions[1].Modifiers
        assertEquals(2, modifiers.size)
        assertEquals("public", modifiers[0])
        assertEquals("static", modifiers[1])
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
        assertEquals("org.springframework.boot.autoconfigure.SpringBootApplication", ds.Annotations[0].Name)
    }

    @Test
    fun should_ident_function_parameter() {
        val resource = this.javaClass.classLoader.getResource("annotation/DemoApplication.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals(2, ds.Functions.size)
        assertEquals("args", ds.Functions[1].Parameters[0].TypeValue)
        assertEquals("java.lang.String[]", ds.Functions[1].Parameters[0].TypeType)
    }

    @Test
    fun should_ident_field() {
        val resource = this.javaClass.classLoader.getResource("inheritance/Child.class")
        val path = Paths.get(resource.toURI()).toFile()

        val ds = ByteCodeParser().parseClassFile(path)
        assertEquals(1, ds.Fields.size)
        assertEquals("hello", ds.Fields[0].TypeValue)
        assertEquals("com.example.demo.Hello", ds.Fields[0].TypeType)
        assertEquals("private", ds.Fields[0].Modifiers[0])
    }

    @Test
    fun should_ident_import() {
        val resource = this.javaClass.classLoader.getResource("annotation/DemoApplication.class")
        val path = Paths.get(resource.toURI()).toFile()
        val ds = ByteCodeParser().parseClassFile(path)

        val imports = ds.Imports
        assertEquals(2, imports.size)

        val firstImport = imports[0]
        assertEquals("org.springframework.boot.SpringApplication", firstImport.Source)

        val secondImport = imports[1]
        assertEquals("org.springframework.boot.autoconfigure.SpringBootApplication", secondImport.Source)
    }

    @Test
    fun should_ident_call() {
        val resource = this.javaClass.classLoader.getResource("annotation/DemoApplication.class")
        val path = Paths.get(resource.toURI()).toFile()
        val ds = ByteCodeParser().parseClassFile(path)

        val calls = ds.Functions[1].FunctionCalls

        assertEquals(1, calls.size)
    }

    @Test
    fun should_ident_kotlin() {
        val resource = this.javaClass.classLoader.getResource("kotlin/QualityGateClientImpl.class")
        val path = Paths.get(resource.toURI()).toFile()
        val ds = ByteCodeParser().parseClassFile(path)


        assertEquals(5, ds.Imports.size)

        assertEquals("org.springframework.web.client.RestTemplate," +
                "com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl," +
                "com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate," +
                "com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient," +
                "org.springframework.stereotype.Component",

            ds.Imports.joinToString(",") { it.Source })

        var hasRestTemplateCall = false
        ds.Functions.forEach { function ->
            function.FunctionCalls.forEach {
                if (it.NodeName == "RestTemplate" && it.FunctionName == "getForObject") {
                    hasRestTemplateCall = true
                }
            }
        }

        assert(hasRestTemplateCall)
    }
}