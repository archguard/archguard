package org.archguard.analyser.sca

import org.junit.jupiter.api.Test
import java.io.File

internal class JavaFinderTest {
    @Test
    internal fun name() {
        val parent = File(".")
        val declTree = JavaFinder().getDeclTree(parent.absolutePath)

        assert(declTree[0].dependencies.size >= 2)
    }
}