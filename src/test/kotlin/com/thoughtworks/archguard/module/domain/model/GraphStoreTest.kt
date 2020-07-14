package com.thoughtworks.archguard.module.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GraphStoreTest {
    lateinit var graphStore: GraphStore<LogicModule>

    @BeforeEach
    internal fun setUp() {
        graphStore = GraphStore()
    }

    @Test
    internal fun should_add_nodes_and_edges() {
        val lg1 = LogicModule("id1", "lg1", listOf(SubModule("s1")))
        val lg2 = LogicModule("id2", "lg2", listOf(SubModule("s2")))
        val lg3 = LogicModule("id3", "lg3", listOf(SubModule("s3")))
        val lg4 = LogicModule("id4", "lg4", listOf(SubModule("s4")))
        val lg5 = LogicModule("id5", "lg5", listOf(SubModule("s5")))
        graphStore.addEdge(lg1, lg2, 3)
        graphStore.addEdge(lg1, lg3, 2)
        graphStore.addEdge(lg2, lg4, 2)
        graphStore.addEdge(lg4, lg3, 1)

        val graph = graphStore.getGraph()
        assertThat(graph.nodes.size).isEqualTo(4)
        assertThat(graph.edges.size).isEqualTo(4)
    }
}