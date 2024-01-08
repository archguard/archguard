package org.archguard.metric;

import org.archguard.graph.Edge
import org.archguard.graph.Graph
import org.archguard.graph.IdNode
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
}
