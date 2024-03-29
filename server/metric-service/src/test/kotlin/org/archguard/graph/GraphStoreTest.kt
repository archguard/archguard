package org.archguard.graph

import org.archguard.arch.LeafManger
import org.archguard.arch.LogicModule
import org.archguard.model.code.JField
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GraphStoreTest {
    lateinit var graphStore: GraphStore

    @BeforeEach
    internal fun setUp() {
        graphStore = GraphStore()
    }

    @Test
    internal fun should_add_nodes_and_edges() {
        val logicModule1 = LogicModule.createWithOnlyLeafMembers("id1", "module1", listOf(LeafManger.createLeaf("submodule1.class")))
        val logicModule2 = LogicModule.createWithOnlyLeafMembers("id2", "module2", listOf(LeafManger.createLeaf("submodule2.class")))
        val logicModule3 = LogicModule.createWithOnlyLeafMembers("id3", "module3", listOf(LeafManger.createLeaf("submodule3.class")))

        graphStore.addEdge(logicModule1, logicModule2, 3)
        graphStore.addEdge(logicModule1, logicModule3, 2)
        graphStore.addEdge(logicModule2, logicModule3, 2)
        graphStore.addEdge(logicModule1, logicModule2, 2)

        val graph = graphStore.getGraph()
        Assertions.assertThat(graph.nodes.size).isEqualTo(3)
        Assertions.assertThat(graph.edges.size).isEqualTo(3)
        Assertions.assertThat(graph.edges).contains(Edge("id1", "id2", 5), Edge("id1", "id3", 2), Edge("id2", "id3", 2))
    }

    @Test
    internal fun `should to undirected graph`() {
        val jField1 = JField("f1", "f1", "String")
        val jField2 = JField("f2", "f2", "Int")
        val jField3 = JField("f3", "f3", "Double")
        val jField4 = JField("f4", "f4", "Double")

        val graph = GraphStore()
        graph.addEdge(jField1, jField2)
        graph.addEdge(jField2, jField1)
        graph.addEdge(jField3, jField4)
        Assertions.assertThat(graph.getGraph().edges.size).isEqualTo(3)
        Assertions.assertThat(graph.toUndirectedGraph().edges.size).isEqualTo(4)
    }
}
