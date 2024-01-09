package org.archguard.graph.code

/**
 * The `PackageStore` class represents a package store that stores information about packages and their relationships.
 * It provides methods to add nodes and edges to the package graph, as well as retrieve the package graph.
 *
 * @property packageNodes The list of package nodes in the store.
 * @property packageEdges The list of package edges in the store.
 * @property index The index used to assign unique IDs to package nodes.
 *
 * @constructor Creates a new instance of the `PackageStore` class.
 */
class PackageStore {

    private var packageNodes: ArrayList<PackageNode> = ArrayList()

    private var packageEdges: ArrayList<PackageEdge> = ArrayList()

    private var index: Int = 0

    fun addNode(name: String): Int {
        return if (nodeContains(name)) {
            getNodeId(name)
        } else {
            index++
            val packageNode: PackageNode = if (!name.contains('.')) {
                PackageNode(index, name, 0)
            } else {
                PackageNode(index, name, getNodeId(name.substringBeforeLast('.')))
            }
            packageNodes.add(packageNode)
            packageNode.id
        }
    }

    private fun getNodeId(name: String): Int {
        return packageNodes.find { it.name == name }?.id ?: addNode(name)
    }

    fun nodeContains(name: String): Boolean {
        return packageNodes.any { it.name == name }
    }

    fun addEdge(a: String, b: String, num: Int) {
        val aId = getNodeId(a)
        val bId = getNodeId(b)
        packageEdges.add(PackageEdge(aId, bId, num))
    }

    fun getPackageGraph(): PackageGraph {
        val indexes = ArrayList<Pair<Int, Int>>()
        packageNodes.sortBy { it.name }
        packageNodes.forEachIndexed { index, node ->
            indexes.add(Pair(node.id, index + 1))
            node.id = index + 1
        }
        packageNodes.forEach {
            it.parent = indexes.find { i -> i.first == it.parent }?.second ?: 0
            it.name = it.name.substringAfterLast('.')
        }
        packageEdges.forEach {
            it.a = indexes.find { i -> i.first == it.a }?.second ?: -1
            it.b = indexes.find { i -> i.first == it.b }?.second ?: -1
        }
        return PackageGraph(packageNodes, packageEdges.filter { it.a != it.b }.sortedBy { i -> i.a * 1000 + i.b })
    }
}
