package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.model.DeclFileTree
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class NpmParserTest {
    private val samplePackageJson = """{
  "name": "my_package",
  "version": "1.0.0",
  "dependencies": {
    "my_dep": "^1.0.0",
    "another_dep": "~2.2.0"
  }
}
""".trimIndent()

    @Test
    fun first_dep() {
        val declFileTree = DeclFileTree("package.json", "package.json", samplePackageJson)
        val declTree = NpmParser().lookupSource(declFileTree)

        assertEquals(1, declTree.size)
        assertEquals("my_package", declTree[0].name)
        assertEquals("1.0.0", declTree[0].version)
        assertEquals(2, declTree[0].dependencies.size)
    }
}