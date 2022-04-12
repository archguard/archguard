package com.thoughtworks.archguard.code.packages.domain


class PackageStore {

    private var packageNodes: ArrayList<PackageNode> = ArrayList()

    private var packageEdges: ArrayList<PackageEdge> = ArrayList()

    private var index: Int = 0

    private fun addNode(name: String): Int {
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

    private fun nodeContains(name: String): Boolean {
        return packageNodes.any { it.name == name }
    }

    fun addEdge(a: String, b: String, num: Int) {
        val aId = getNodeId(a)
        val bId = getNodeId(b)
        packageEdges.add(PackageEdge(aId, bId, num))
    }

    fun getPackageGraph(): PackageGraph {
        val indexs = ArrayList<Pair<Int, Int>>()
        packageNodes.sortBy { it.name }
        packageNodes.forEachIndexed { index, node ->
            indexs.add(Pair(node.id, index + 1))
            node.id = index + 1
        }
        packageNodes.forEach {
            it.parent = indexs.find { i -> i.first == it.parent }?.second ?: 0
            it.name = it.name.substringAfterLast('.')
        }
        packageEdges.forEach {
            it.a = indexs.find { i -> i.first == it.a }?.second ?: -1
            it.b = indexs.find { i -> i.first == it.b }?.second ?: -1
        }
        return PackageGraph(packageNodes, packageEdges.filter { it.a != it.b }.sortedBy { i -> i.a * 1000 + i.b })
    }

}

data class ModulePackage(val module: String, val packageGraph: PackageGraph)
data class PackageGraph(var nodes: List<PackageNode>, var edges: List<PackageEdge>)
data class PackageEdge(var a: Int, var b: Int, var num: Int)
data class PackageNode(var id: Int, var name: String, var parent: Int)

