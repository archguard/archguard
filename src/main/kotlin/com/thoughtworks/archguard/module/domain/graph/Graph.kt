package com.thoughtworks.archguard.module.domain.graph

class GraphStore {
    private val nodes = mutableListOf<Node>()
    private val edges = mutableListOf<Edge>()

    fun addEdge(caller: Node, callee: Node, num: Int) {
        if (!contains(caller)) {
            nodes.add(caller)
        }
        if (!contains(callee)) {
            nodes.add(callee)
        }
        edges.add(Edge(caller.getNodeId(), callee.getNodeId(), num))
    }

    private fun contains(caller: Node): Boolean {
        return nodes.map { it.getNodeId() }.contains(caller.getNodeId())
    }

    fun getGraph(): Graph {
        return Graph(nodes.distinctBy { it.getNodeId() }, edges)
    }
}

data class Graph(val nodes: List<Node>, val edges: List<Edge>)
data class Edge(val a: String, val b: String, val num: Int)
interface Node {
    fun getNodeId(): String
}
