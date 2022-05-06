package org.archguard.analyser.sca.processor

import org.junit.jupiter.api.Test
import java.io.File

internal class JavaFinderTest {
    @Test
    internal fun root_tree() {
        val declTree2 = JavaFinder().buildDeclTree(File("..").canonicalPath)
        assert(declTree2!!.childrens.size > 5)
    }
}
