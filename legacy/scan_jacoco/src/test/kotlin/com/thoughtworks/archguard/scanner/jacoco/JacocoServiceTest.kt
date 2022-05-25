package com.thoughtworks.archguard.scanner.jacoco

import org.junit.jupiter.api.Test
import java.io.File

internal class JacocoServiceTest {

    @Test
    fun readJacoco() {
        val resource = this.javaClass.classLoader.getResource("parent").path
        JacocoService(Bean2Sql()).readJacoco(Config(projectPath = resource, bin = "classes", exec = "jacoco/jacoco.exec"))

        assert(File("jacoco.sql").exists())
        
        val text = File("jacoco.sql").readText()
        assert(text.contains("Model.kt"))
    }
}