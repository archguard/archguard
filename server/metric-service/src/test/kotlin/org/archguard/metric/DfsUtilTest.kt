package org.archguard.metric;

import org.archguard.graph.*
import org.archguard.model.code.JField
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DfsUtilTest {
    @Test
    fun should_CreateDfsUtilObject_When_UndirectedGraphIsProvided() {
        // given
        val nodes = listOf(IdNode("A"), IdNode("B"), IdNode("C"))
        val edges = listOf(Edge("A", "B", 1), Edge("B", "C", 1))
        val undirectedGraph = Graph(nodes, edges)

        // when
        val dfsUtil = DfsUtil(undirectedGraph)

        // then
        assertEquals(1, dfsUtil.getConnectivityCount())
    }

    @Test
    fun should_ReturnZeroConnectivityCount_When_EmptyGraphIsProvided() {
        // given
        val nodes = emptyList<IdNode>()
        val edges = emptyList<Edge>()
        val undirectedGraph = Graph(nodes, edges)

        // when
        val dfsUtil = DfsUtil(undirectedGraph)

        // then
        assertEquals(0, dfsUtil.getConnectivityCount())
    }

    @Test
    fun should_ReturnOneConnectivityCount_When_SingleIdNodeGraphIsProvided() {
        // given
        val nodes = listOf(IdNode("A"))
        val edges = emptyList<Edge>()
        val undirectedGraph = Graph(nodes, edges)

        // when
        val dfsUtil = DfsUtil(undirectedGraph)

        // then
        assertEquals(1, dfsUtil.getConnectivityCount())
    }

    @Test
    fun should_ReturnTwoConnectivityCount_When_TwoDisconnectedIdNodesGraphIsProvided() {
        // given
        val nodes = listOf(IdNode("A"), IdNode("B"))
        val edges = emptyList<Edge>()
        val undirectedGraph = Graph(nodes, edges)

        // when
        val dfsUtil = DfsUtil(undirectedGraph)

        // then
        assertEquals(2, dfsUtil.getConnectivityCount())
    }

    @Test
    fun should_ReturnOneConnectivityCount_When_TwoConnectedIdNodesGraphIsProvided() {
        // given
        val nodes = listOf(IdNode("A"), IdNode("B"))
        val edges = listOf(Edge("A", "B", 1))
        val undirectedGraph = Graph(nodes, edges)

        // when
        val dfsUtil = DfsUtil(undirectedGraph)

        // then
        assertEquals(1, dfsUtil.getConnectivityCount())
    }

    @Test
    internal fun `should get connectivity count of graph`() {
        val jField1 = JField("f1", "f1", "String")
        val jField2 = JField("f2", "f2", "Int")
        val jField3 = JField("f3", "f3", "Double")
        val jField4 = JField("f4", "f4", "Double")
        val jField5 = JField("f5", "f5", "Double")
        val jField6 = JField("f6", "f6", "Double")

        val graph = GraphStore()
        val dfsService0 = DfsUtil(graph.toUndirectedGraph())
        assertThat(dfsService0.getConnectivityCount()).isEqualTo(0)

        graph.addEdge(jField1, jField2)
        graph.addEdge(jField3, jField4)
        graph.addEdge(jField5, jField6)

        val dfsService1 = DfsUtil(graph.toUndirectedGraph())
        assertThat(dfsService1.getConnectivityCount()).isEqualTo(3)

        graph.addEdge(jField1, jField3)
        val dfsService2 = DfsUtil(graph.toUndirectedGraph())
        assertThat(dfsService2.getConnectivityCount()).isEqualTo(2)
    }
}
