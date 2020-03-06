package com.thoughtworks.archguard.archguardpackage.domain.util

import com.thoughtworks.archguard.archguardpackage.domain.dto.Edge
import com.thoughtworks.archguard.archguardpackage.domain.dto.Node
import com.thoughtworks.archguard.archguardpackage.domain.dto.PackageGraph

class PackageStore {

    private var nodes: ArrayList<Node> = ArrayList()

    private var edges: ArrayList<Edge> = ArrayList()

    private var index: Int = 0

    private fun addNode(name: String): Int {
        return if (nodeContains(name)) {
            getNodeId(name)
        } else {
            index++
            val node: Node = if (!name.contains('.')) {
                Node(index, name, 0)
            } else {
                Node(index, name, getNodeId(name.substringBeforeLast('.')))
            }
            nodes.add(node)
            node.id
        }
    }

    private fun getNodeId(name: String): Int {
        return nodes.find { it.name == name }?.id ?: addNode(name)
    }

    private fun nodeContains(name: String): Boolean {
        return nodes.any { it.name == name }
    }

    fun addEdge(a: String, b: String, num: Int) {
        val aId = getNodeId(a)
        val bId = getNodeId(b)
        edges.add(Edge(aId, bId, num))
    }

    fun getPackageGraph(): PackageGraph {
        val indexs = ArrayList<Pair<Int, Int>>()
        nodes.sortBy { it.name }
        nodes.forEachIndexed { index, node ->
            indexs.add(Pair(node.id, index + 1))
            node.id = index + 1
        }
        nodes.forEach {
            it.parent = indexs.find { i -> i.first == it.parent }?.second ?: 0
            it.name = it.name.substringAfterLast('.')
        }
        edges.forEach {
            it.a = indexs.find { i -> i.first == it.a }?.second ?: -1
            it.b = indexs.find { i -> i.first == it.b }?.second ?: -1
        }
        return PackageGraph(nodes, edges.filter { it.a != it.b}.sortedBy { i -> i.a * 1000 + i.b})
    }

}