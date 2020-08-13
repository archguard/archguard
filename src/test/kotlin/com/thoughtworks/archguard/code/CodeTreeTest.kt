package com.thoughtworks.archguard.code

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CodeTreeTest {
    @Test
    internal fun `add nodes`() {
        val codeTree = CodeTree()
        codeTree.addClass("a.b.d")
        codeTree.addClass("a.b.e.f")
        codeTree.addClass("a.c")
        codeTree.addClass("a.c")
        codeTree.addClass("m.n.p")
        codeTree.addClass("m.q")
        codeTree.addClass("x")
        codeTree.addClass("x")
        codeTree.addClass("a")
        codeTree.addClass("a")

        assertThat(codeTree.trees).containsExactlyInAnyOrderElementsOf(listOf(
                Node("a", TypeEnum.PACKAGE), Node("m", TypeEnum.PACKAGE), Node("x", TypeEnum.FILE), Node("a", TypeEnum.FILE)))

        assertThat(codeTree.trees.first { it == Node("a", TypeEnum.PACKAGE) }.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("b", TypeEnum.PACKAGE), Node("c", TypeEnum.FILE)))
        assertThat(codeTree.trees.first { it == Node("a", TypeEnum.PACKAGE) }.children.first { it.node == "b" }.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("d", TypeEnum.FILE), Node("e", TypeEnum.PACKAGE)))
        assertThat(codeTree.trees.first { it == Node("a", TypeEnum.PACKAGE) }.children.first { it.node == "b" }.children.first { it.node == "e" }.children)
                .containsOnly(Node("f", TypeEnum.FILE))

        assertThat(codeTree.trees.first { it == Node("m", TypeEnum.PACKAGE) }.children)
                .containsExactlyInAnyOrderElementsOf(listOf(Node("n", TypeEnum.PACKAGE), Node("q", TypeEnum.FILE)))
        assertThat(codeTree.trees.first { it == Node("m", TypeEnum.PACKAGE) }.children.first { it == Node("n", TypeEnum.PACKAGE) }.children)
                .containsOnly(Node("p", TypeEnum.FILE))
    }
}