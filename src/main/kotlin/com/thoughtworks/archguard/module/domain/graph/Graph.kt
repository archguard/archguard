package com.thoughtworks.archguard.module.domain.graph

class GraphStore {
    private val nodes = mutableListOf<Node>()
    private val edges = mutableListOf<Edge>()

    fun addEdge(caller: Node, callee: Node, num: Int) {
        nodes.add(callee)
        nodes.add(caller)
        edges.add(Edge(caller.getNodeId(), callee.getNodeId(), num))
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
