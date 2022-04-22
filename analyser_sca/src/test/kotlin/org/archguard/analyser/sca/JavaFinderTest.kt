package org.archguard.analyser.sca

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class JavaFinderTest {
    @Test
    internal fun count_self_module_deps() {
        val declTree = JavaFinder().byGradleFiles(File(".").absolutePath)
        assert(declTree[0].dependencies.size >= 2)
    }

    @Test
    internal fun count_maven_module_deps() {
        val declTree = JavaFinder().byMavenFiles(File(".").absolutePath)
        val deps = declTree.flatMap {
            it.dependencies
        }.toTypedArray()

        assert(deps.isNotEmpty())
    }

    @Test
    internal fun root_tree() {
        val declTree2 = JavaFinder().buildDeclTree(File("..").canonicalPath)
        assert(declTree2!!.childrens.size > 5)
    }

    @Test
    internal fun maven_with_gradle() {
        val decls = JavaFinder().find(File(".").canonicalPath)
        // contains build resources, so will > 2
        assert(decls.size >= 2)
    }
}
