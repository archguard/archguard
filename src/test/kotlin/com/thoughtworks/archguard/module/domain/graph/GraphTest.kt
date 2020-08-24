package com.thoughtworks.archguard.module.domain.graph

import com.thoughtworks.archguard.module.domain.model.LogicComponent
import com.thoughtworks.archguard.module.domain.model.LogicModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GraphTest {
    lateinit var graphStore: GraphStore

    @BeforeEach
    internal fun setUp() {
        graphStore = GraphStore()
    }

    @Test
    internal fun should_add_nodes_and_edges() {
        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LogicComponent.createLeaf("submodule1.class")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LogicComponent.createLeaf("submodule2.class")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LogicComponent.createLeaf("submodule3.class")))

        graphStore.addEdge(logicModule1, logicModule2, 3)
        graphStore.addEdge(logicModule1, logicModule3, 2)
        graphStore.addEdge(logicModule2, logicModule3, 2)
        graphStore.addEdge(logicModule1, logicModule2, 2)

        val graph = graphStore.getGraph()
        assertThat(graph.nodes.size).isEqualTo(3)
        assertThat(graph.edges.size).isEqualTo(3)
        assertThat(graph.edges).contains(Edge("id1", "id2", 5), Edge("id1", "id3", 2), Edge("id2", "id3", 2))
    }
}
