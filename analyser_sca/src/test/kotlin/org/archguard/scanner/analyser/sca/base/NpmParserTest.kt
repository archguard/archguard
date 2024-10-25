package org.archguard.scanner.analyser.sca.base

import org.archguard.context.DEP_SCOPE
import org.archguard.context.DeclFileTree
import org.archguard.scanner.analyser.sca.npm.NpmParser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class NpmParserTest {
    private val samplePackageJson = """{
  "name": "my_package",
  "version": "1.0.0",
  "dependencies": {
    "my_dep": "^1.0.0",
    "another_dep": "~2.2.0"
  },
  "devDependencies": {
    "another_dep": "~2.2.0"
  }
}
""".trimIndent()

    @Test
    fun normal_dep() {
        val declFileTree = DeclFileTree("package.json", "package.json", samplePackageJson)
        val declTree = NpmParser().lookupSource(declFileTree)

        assertEquals(1, declTree.size)
        assertEquals("my_package", declTree[0].name)
        assertEquals("1.0.0", declTree[0].version)
        assertEquals(3, declTree[0].dependencies.size)
        assertEquals(DEP_SCOPE.NORMAL, declTree[0].dependencies[0].scope)
    }

    @Test
    fun dev_scope() {
        val declFileTree = DeclFileTree("package.json", "package.json", samplePackageJson)
        val declTree = NpmParser().lookupSource(declFileTree)

        assertEquals(3, declTree[0].dependencies.size)
        assertEquals(DEP_SCOPE.DEV, declTree[0].dependencies[2].scope)
    }
}
