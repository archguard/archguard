package org.archguard.analyser.sca

import org.junit.jupiter.api.Test
import java.io.File

internal class JavaFinderTest {
    @Test
    internal fun name() {

        val declTree = JavaFinder().getDeclTree(File(".").absolutePath)

        assert(declTree[0].dependencies.size >= 2)

        val declTree2 = JavaFinder().buildDeclTree(File("..").canonicalPath)
        println(declTree2)
    }
}