package com.thoughtworks.archguard.dependence_package.domain.util

import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleEdge
import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleGraph
import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleNode

class ModuleStore {

    private var moduleNodes: ArrayList<ModuleNode> = ArrayList()

    private var moduleEdges: ArrayList<ModuleEdge> = ArrayList()

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