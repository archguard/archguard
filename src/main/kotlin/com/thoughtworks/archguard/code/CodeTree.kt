package com.thoughtworks.archguard.code

class CodeTree {
    var trees: HashSet<Node> = HashSet()

    fun addClass(nodeName: String) {
        val split = nodeName.split(".")
        val classTopPackage = split[0]
        if (split.size == 1) {
            trees.add(Node(classTopPackage, TypeEnum.FILE))
            return
        }
        if (!trees.contains(Node(classTopPackage, TypeEnum.PACKAGE))) {
            val topNode = Node(classTopPackage, TypeEnum.PACKAGE)
            addOrUpdateNodes(topNode, split)
            trees.add(topNode)
            return
        }
        val topNode = trees.first { it == Node(classTopPackage, TypeEnum.PACKAGE) }
        addOrUpdateNodes(topNode, split)
    }

    fun fixTopNodeSubModuleType() {
        trees.forEach { it.type = TypeEnum.SUB_MODULE }
    }

    fun getHeadNodes(): Set<Node> {
        return trees
    }

    fun getNextPackagesOrClasses(node: Node): Set<Node> {
        return node.children
    }

    private fun addOrUpdateNodes(topNode: Node, split: List<String>) {
        var currentNode: Node = topNode
        for (node in split.subList(1, split.size - 1)) {
            val nextNode = Node(node, TypeEnum.PACKAGE)
            currentNode.children.add(nextNode)
            currentNode = currentNode.children.find { it.node == node }!!
        }
        val leafNode = Node(split[split.size - 1], TypeEnum.FILE)
        currentNode.children.add(leafNode)
    }
}