package com.thoughtworks.archguard.code.clazz.domain

import org.archguard.graph.TypeEnum
import org.archguard.graph.TypedNode
import org.archguard.model.code.CodeTree
import org.assertj.core.api.Assertions
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

        Assertions.assertThat(codeTree.trees).containsExactlyInAnyOrderElementsOf(
            listOf(
                TypedNode("a", TypeEnum.PACKAGE), TypedNode("m", TypeEnum.PACKAGE), TypedNode("x", TypeEnum.FILE), TypedNode("a", TypeEnum.FILE)
            )
        )

        Assertions.assertThat(codeTree.trees.first { it == TypedNode("a", TypeEnum.PACKAGE) }.children)
            .containsExactlyInAnyOrderElementsOf(listOf(TypedNode("b", TypeEnum.PACKAGE), TypedNode("c", TypeEnum.FILE)))
        Assertions.assertThat(
            codeTree.trees.first {
                it == TypedNode(
                    "a",
                    TypeEnum.PACKAGE
                )
            }.children.first { it.node == "b" }.children
        )
            .containsExactlyInAnyOrderElementsOf(listOf(TypedNode("d", TypeEnum.FILE), TypedNode("e", TypeEnum.PACKAGE)))
        Assertions.assertThat(
            codeTree.trees.first {
                it == TypedNode(
                    "a",
                    TypeEnum.PACKAGE
                )
            }.children.first { it.node == "b" }.children.first { it.node == "e" }.children
        )
            .containsOnly(TypedNode("f", TypeEnum.FILE))

        Assertions.assertThat(codeTree.trees.first { it == TypedNode("m", TypeEnum.PACKAGE) }.children)
            .containsExactlyInAnyOrderElementsOf(listOf(TypedNode("n", TypeEnum.PACKAGE), TypedNode("q", TypeEnum.FILE)))
        Assertions.assertThat(
            codeTree.trees.first {
                it == TypedNode(
                    "m",
                    TypeEnum.PACKAGE
                )
            }.children.first { it == TypedNode("n", TypeEnum.PACKAGE) }.children
        )
            .containsOnly(TypedNode("p", TypeEnum.FILE))
    }
}
