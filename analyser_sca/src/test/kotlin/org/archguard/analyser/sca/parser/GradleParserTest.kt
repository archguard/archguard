package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.model.DEP_SCOPE
import org.archguard.analyser.sca.model.DeclFile
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
    internal fun normal_match() {
        val declFile = DeclFile("archguard", "build.gradle", gradleSample)
        val depDecls = GradleParser().lookupSource(declFile)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(2, dependencies.size)
        assertEquals("joda-time:joda-time", dependencies[0].name)
        assertEquals("2.2", dependencies[0].version)

        assertEquals("junit", dependencies[1].group)
        assertEquals("junit", dependencies[1].artifact)
        assertEquals("4.12", dependencies[1].version)
        assertEquals(DEP_SCOPE.TEST, dependencies[1].scope)
    }

    @Test
    internal fun keyword_arg() {
        val declFile = DeclFile("archguard", "build.gradle", """
dependencies {
    runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
}
        """.trimIndent())
        val depDecls = GradleParser().lookupSource(declFile)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(1, dependencies.size)
        assertEquals(DEP_SCOPE.RUNTIME, dependencies[0].scope)
        assertEquals("org.springframework", dependencies[0].group)
        assertEquals("spring-core", dependencies[0].artifact)
        assertEquals("2.5", dependencies[0].version)
    }

    @Test
    internal fun dependency_set() {
        val declFile = DeclFile("archguard", "build.gradle", """
dependencySet(group:'org.slf4j', version: '1.7.7') { 
    entry 'slf4j-api' 
}
        """.trimIndent())
        val depDecls = GradleParser().lookupSource(declFile)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(1, dependencies.size)
    }
}