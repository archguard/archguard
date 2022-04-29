package org.archguard.architecture

import org.archguard.analyser.sca.model.DependencyEntry
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.architecture.detect.FrameworkMarkup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArchitectureDetectTest {
    @Test
    internal fun name() {
        val markup = FrameworkMarkup.byLanguage("Java")!!
        val dependencyEntry = DependencyEntry(
            name = "org.springframework.boot:spring-boot-starter-jdbc",
            group = "org.springframework.boot",
            artifact = "spring-boot-starter-jdbc",
            version = ""
        )
        val packageDependencies = PackageDependencies(
            name = "",
            version = "",
            packageManager = "Gradle",
            dependencies = listOf(dependencyEntry)
        )

        val potentialExecArch = ArchitectureDetect().detectAppType(markup, packageDependencies)
        assertEquals("web", potentialExecArch.appTypes[0])
    }
}