package org.archguard.graph

/**
 * 将无向图转换为邻接矩阵表示的图。之后进行DFS，计算连通子图个数
 */
class DfsUtil(undirectedGraph: Graph) {
    private var vertices: Array<String> = undirectedGraph.nodes.map { it.getNodeId() }.toTypedArray()
    private var arcs: Array<IntArray>
    private var vexnum = 0
    private var visited: BooleanArray

    // 连通子图个数
    private var connectivityCount = 0

    init {
        this.vexnum = undirectedGraph.nodes.size
        this.visited = BooleanArray(this.vexnum)
        this.arcs = Array(this.vexnum) { IntArray(this.vexnum) }
        undirectedGraph.edges.forEach { addEdge(it) }
        dfs()
    }

    private fun addEdge(edge: Edge) {
        val a = edge.a
        val b = edge.b
        val indexOfA = vertices.indexOfFirst { it == a }
        val indexOfB = vertices.indexOfFirst { it == b }
        arcs[indexOfA][indexOfB] = 1
        arcs[indexOfB][indexOfA] = 1
    }

    fun dfs() {
        for (i in 0 until vexnum) {
            if (!visited[i]) {
                // 若是连通图，只会执行一次
                traverse(i)
                connectivityCount++
            }
        }
    }

    private fun traverse(i: Int) {
        visited[i] = true
        for (j in 0 until vexnum) {
            if (arcs[i][j] == 1 && !visited[j]) {
                traverse(j)
            }
        }
    }

    fun getConnectivityCount(): Int {
        return connectivityCount
    }
}
