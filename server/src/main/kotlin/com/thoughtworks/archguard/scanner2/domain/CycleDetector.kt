package com.thoughtworks.archguard.scanner2.domain

import org.archguard.graph.Edge
import org.archguard.graph.Graph
import com.thoughtworks.archguard.scanner2.domain.model.IdNode
import org.archguard.graph.Node
import java.util.Stack

/**
 * Reference: https://www.baeldung.com/cs/detecting-cycles-in-directed-graph
 */
class CycleDetector(directedGraph: Graph) {
    private var vertices: Array<String> = directedGraph.nodes.map { it.getNodeId() }.toTypedArray()
    private var arcs: Array<IntArray>
    private var vexnum = 0
    private var visited: Array<VisitStatus>
    private val cycles: MutableList<MutableList<Node>> = mutableListOf()
    private var stack = Stack<Int>()

    init {
        this.vexnum = directedGraph.nodes.size
        this.visited = Array(this.vexnum) { VisitStatus.NOT_VISITED }
        this.arcs = Array(this.vexnum) { IntArray(this.vexnum) }
        directedGraph.edges.forEach { addEdge(it) }
    }

    private fun addEdge(edge: Edge) {
        val a = edge.a
        val b = edge.b
        val indexOfA = vertices.indexOfFirst { it == a }
        val indexOfB = vertices.indexOfFirst { it == b }
        arcs[indexOfA][indexOfB] = 1
    }

    fun findCycles(): List<List<Node>> {
        for (index in vertices.indices) {
            if (this.visited[index] == VisitStatus.NOT_VISITED) {
                stack = Stack<Int>()
                stack.push(index)
                this.visited[index] = VisitStatus.IN_STACK
                processDFSTree()
            }
        }
        return cycles
    }

    private fun processDFSTree() {
        val vertices = vertices.indices.filter { this.arcs[stack.peek()][it] != 0 }
        for (nodeIndex in vertices) {
            if (this.visited[nodeIndex] == VisitStatus.IN_STACK) {
                addCycle(nodeIndex)
            } else if (this.visited[nodeIndex] == VisitStatus.NOT_VISITED) {
                stack.push(nodeIndex)
                this.visited[nodeIndex] = VisitStatus.IN_STACK
                processDFSTree()
            }
        }
        this.visited[this.stack.pop()] = VisitStatus.DONE
    }

    private fun addCycle(nodeIndex: Int) {
        val stack2 = Stack<Int>()
        stack2.push(stack.pop())
        while (stack2.peek() != nodeIndex) {
            stack2.push(stack.pop())
        }
        val cycle = mutableListOf<Node>()
        while (stack2.isNotEmpty()) {
            cycle.add(IdNode(this.vertices[stack2.peek()]))
            stack.push(stack2.pop())
        }
        if (cycle.isNotEmpty()) {
            this.cycles.add(cycle)
        }
    }
}

enum class VisitStatus {
    IN_STACK, NOT_VISITED, DONE
}
