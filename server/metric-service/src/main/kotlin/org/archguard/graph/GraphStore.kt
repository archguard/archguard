package org.archguard.graph

class GraphStore {
    private val nodes = mutableListOf<Node>()
    private var edges = mutableListOf<Edge>()

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

    fun toDirectedGraph(): Graph {
        return Graph(nodes, edges)
    }

    fun getConnectivityCount(): Int {
        return DfsUtil(this.toUndirectedGraph()).getConnectivityCount()
    }

    fun toUndirectedGraph(): Graph {
        val undirectedEdges: MutableList<Edge> = mutableListOf()
        edges.forEach { undirectedEdges.add(it) }
        edges.forEach { undirectedEdges.add(Edge(it.b, it.a, it.num)) }
        // 无向图没有权重
        undirectedEdges.forEach { it.num = 1 }
        return Graph(nodes, undirectedEdges.toSet().toList())
    }
}