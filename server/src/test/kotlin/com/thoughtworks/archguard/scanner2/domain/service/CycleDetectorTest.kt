package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.CycleDetector
import org.archguard.graph.GraphStore
import com.thoughtworks.archguard.scanner2.domain.model.IdNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CycleDetectorTest {
    @Test
    fun should_find_no_cycle_in_graph() {
        val graph = GraphStore()
        val nodeA = IdNode("a")
        val nodeB = IdNode("b")
        val nodeC = IdNode("c")
        val nodeE = IdNode("e")

        graph.addEdge(nodeA, nodeB)
        graph.addEdge(nodeA, nodeE)
        graph.addEdge(nodeB, nodeC)
        graph.addEdge(nodeC, nodeE)
        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        assertThat(cycles.size).isEqualTo(0)
    }

    @Test
    fun should_find_one_cycle_in_graph() {
        val graph = GraphStore()
        val nodeA = IdNode("a")
        val nodeB = IdNode("b")
        val nodeC = IdNode("c")
        val nodeE = IdNode("e")

        graph.addEdge(nodeA, nodeB)
        graph.addEdge(nodeB, nodeC)
        graph.addEdge(nodeC, nodeA)
        graph.addEdge(nodeC, nodeE)
        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        assertThat(cycles.size).isEqualTo(1)
        assertThat(cycles[0]).containsExactlyInAnyOrderElementsOf(listOf(nodeA, nodeB, nodeC))
    }

    @Test
    fun should_find_two_cycle_in_graph() {
        val graph = GraphStore()
        val nodeA = IdNode("a")
        val nodeB = IdNode("b")
        val nodeC = IdNode("c")
        val nodeE = IdNode("e")

        val nodeM = IdNode("m")
        val nodeN = IdNode("n")
        val nodeP = IdNode("p")
        val nodeQ = IdNode("q")

        graph.addEdge(nodeA, nodeB)
        graph.addEdge(nodeB, nodeC)
        graph.addEdge(nodeC, nodeA)
        graph.addEdge(nodeC, nodeE)
        graph.addEdge(nodeM, nodeN)
        graph.addEdge(nodeN, nodeP)
        graph.addEdge(nodeP, nodeQ)
        graph.addEdge(nodeQ, nodeM)

        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        assertThat(cycles.size).isEqualTo(2)
        assertThat(cycles).containsExactlyInAnyOrderElementsOf(listOf(listOf(nodeA, nodeB, nodeC), listOf(nodeM, nodeN, nodeP, nodeQ)))
    }

    @Test
    fun should_find_cycles_in_graph_while_one_node_appear_in_two_cycle() {
        val graph = GraphStore()
        val nodeA = IdNode("a")
        val nodeB = IdNode("b")
        val nodeC = IdNode("c")
        val nodeE = IdNode("e")

        val nodeM = IdNode("m")
        val nodeN = IdNode("n")
        val nodeP = IdNode("p")
        val nodeQ = IdNode("q")

        graph.addEdge(nodeA, nodeB)
        graph.addEdge(nodeB, nodeC)
        graph.addEdge(nodeC, nodeA)
        graph.addEdge(nodeC, nodeE)
        graph.addEdge(nodeE, nodeA)
        graph.addEdge(nodeM, nodeN)
        graph.addEdge(nodeN, nodeP)
        graph.addEdge(nodeP, nodeQ)
        graph.addEdge(nodeQ, nodeM)

        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        assertThat(cycles.size).isEqualTo(3)
        assertThat(cycles).containsExactlyInAnyOrderElementsOf(listOf(listOf(nodeA, nodeB, nodeC), listOf(nodeA, nodeB, nodeC, nodeE), listOf(nodeM, nodeN, nodeP, nodeQ)))
    }
}
