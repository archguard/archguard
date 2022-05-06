package org.archguard.analyser.sca.maven

import org.archguard.analyser.sca.gradle.GradleFinder
import org.junit.jupiter.api.Test
import java.io.File

internal class MavenFinderTest {
    @Test
    internal fun count_self_module_deps() {
        val declTree = GradleFinder().process(File(".").absolutePath)
        assert(declTree[0].dependencies.size >= 2)
    }
}