package org.archguard.scanner.dep.parser

import org.archguard.scanner.dep.model.DeclFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GradleParserTest {
    private val gradleSample = """
sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    implementation "joda-time:joda-time:2.2"
    testImplementation "junit:junit:4.12"
}
""".trimIndent()

    @Test
    internal fun shouldMatch() {
        val declFile = DeclFile("archguard", "pom.xml", gradleSample)
        val depDecls = GradleParser().lookupSource(declFile)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(2, dependencies.size)
        assertEquals("joda-time:joda-time", dependencies[0].name)
        assertEquals("2.2", dependencies[0].version)

        assertEquals("junit", dependencies[1].group[0])
        assertEquals("junit", dependencies[1].artifact)
        assertEquals("4.12", dependencies[1].version)
    }
}