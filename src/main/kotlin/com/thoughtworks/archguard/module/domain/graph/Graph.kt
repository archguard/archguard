package com.thoughtworks.archguard.module.domain.graph

class GraphStore {
    private val nodes = mutableListOf<Node>()
    private val edges = mutableListOf<Edge>()

    @Synchronized
    fun addEdge(caller: Node, callee: Node) {
        addEdge(caller, callee, 1)
    }

    @Synchronized
    fun addEdge(caller: Node, callee: Node, num: Int) {
        if (!contains(caller)) {
            nodes.add(caller)
        }
        if (!contains(callee)) {
            nodes.add(callee)
        }
        if (!existEdge(caller, callee)) {
            edges.add(Edge(caller.getNodeId(), callee.getNodeId(), num))
            return
        }
        updateEdgeByAddNum(caller, callee, num)
    }

    private fun updateEdgeByAddNum(caller: Node, callee: Node, num: Int) {
        val oldEdge = edges.first { it.a == caller.getNodeId() && it.b == callee.getNodeId() }
        val newEdge = Edge(caller.getNodeId(), callee.getNodeId(), num + oldEdge.num)
        edges.remove(oldEdge)
        edges.add(newEdge)
    }

    private fun existEdge(caller: Node, callee: Node): Boolean {
        return edges.any { it.a == caller.getNodeId() && it.b == callee.getNodeId() }
    }

    private fun contains(caller: Node): Boolean {
        return nodes.map { it.getNodeId() }.contains(caller.getNodeId())
    }

    fun getGraph(): Graph {
        return Graph(nodes, edges)
    }
}

data class Graph(val nodes: List<Node>, val edges: List<Edge>)
data class Edge(val a: String, val b: String, val num: Int)
interface Node {
    fun getNodeId(): String
}
