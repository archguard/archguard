package org.archguard.scanner.analyser

import org.archguard.scanner.analyser.sca.gradle.GradleFinder
import org.archguard.scanner.analyser.sca.maven.MavenFinder
import org.junit.jupiter.api.Test
import java.io.File

internal class RunnerTest {
    @Test
    internal fun gradle_with_maven() {
        val path = File(".").canonicalPath
        val deps = GradleFinder().process(path).toMutableList()
        deps += MavenFinder().process(path)

        assert(deps.size >= 2)
    }
}
