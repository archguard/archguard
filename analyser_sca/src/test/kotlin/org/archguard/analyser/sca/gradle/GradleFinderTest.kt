package org.archguard.analyser.sca.gradle

import org.archguard.analyser.sca.maven.MavenFinder
import org.junit.jupiter.api.Test
import java.io.File

internal class GradleFinderTest {
    @Test
    internal fun count_maven_module_deps() {
        val declTree = MavenFinder().process(File(".").absolutePath)
        val deps = declTree.flatMap {
            it.dependencies
        }.toTypedArray()

        assert(deps.isNotEmpty())
    }
}