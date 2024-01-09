package org.archguard.model.code

import org.archguard.graph.TypeEnum
import org.archguard.graph.TypedNode

class CodeTree {
    var trees: HashSet<TypedNode> = HashSet()

    fun addClass(nodeName: String) {
        val split = nodeName.split(".")
        var classTopPackage = split[0]
        if (classTopPackage == "") {
            classTopPackage = "."
        }

        if (split.size == 1) {
            trees.add(TypedNode(classTopPackage, TypeEnum.FILE))
            return
        }
        if (!trees.contains(TypedNode(classTopPackage, TypeEnum.PACKAGE))) {
            val topTypedNode = TypedNode(classTopPackage, TypeEnum.PACKAGE)
            addOrUpdateNodes(topTypedNode, split)
            trees.add(topTypedNode)
            return
        }
        val topTypedNode = trees.first { it == TypedNode(classTopPackage, TypeEnum.PACKAGE) }
        addOrUpdateNodes(topTypedNode, split)
    }

    fun fixTopNodeSubModuleType() {
        trees.forEach { it.type = TypeEnum.SUB_MODULE }
    }

    fun getNextPackagesOrClasses(typedNode: TypedNode): Set<TypedNode> {
        return typedNode.children
    }

    private fun addOrUpdateNodes(topTypedNode: TypedNode, split: List<String>) {
        var currentTypedNode: TypedNode = topTypedNode
        for (node in split.subList(1, split.size - 1)) {
            val nextTypedNode = TypedNode(node, TypeEnum.PACKAGE)
            currentTypedNode.children.add(nextTypedNode)
            currentTypedNode = currentTypedNode.children.find { it.node == node }!!
        }
        val leafTypedNode = TypedNode(split[split.size - 1], TypeEnum.FILE)
        currentTypedNode.children.add(leafTypedNode)
    }
}
