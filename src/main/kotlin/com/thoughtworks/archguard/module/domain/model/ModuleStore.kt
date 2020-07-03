package com.thoughtworks.archguard.module.domain.model

class ModuleStore {

    private val moduleNodes: ArrayList<ModuleNode> = ArrayList()

    private val moduleEdges: ArrayList<ModuleEdge> = ArrayList()

    private var index: Int = 0

    private fun addNode(name: String): Int {
        index++
        val node = ModuleNode(index, name)
        moduleNodes.add(node)
        return node.id
    }

    private fun getNodeId(name: String): Int {
        return moduleNodes.find { it.name == name }?.id ?: addNode(name)
    }

    fun addEdge(a: String, b: String, num: Int) {
        val aId = getNodeId(a)
        val bId = getNodeId(b)
        moduleEdges.add(ModuleEdge(aId, bId, num))
    }

    fun getModuleGraph(): ModuleGraph {
//        moduleNodes.sortBy { it.id }
        return ModuleGraph(moduleNodes, moduleEdges.filter { it.a != it.b })
    }

}

data class ModuleNode(var id: Int, var name: String)
data class ModuleGraph(var nodes: List<ModuleNode>, var edges: List<ModuleEdge>)
data class ModuleEdge(var a: Int, var b: Int, var num: Int)
