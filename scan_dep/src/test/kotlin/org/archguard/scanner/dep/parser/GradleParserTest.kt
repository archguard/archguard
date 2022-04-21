package org.archguard.scanner.dep.parser

import org.junit.jupiter.api.Test

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
    internal fun name() {

    }
}