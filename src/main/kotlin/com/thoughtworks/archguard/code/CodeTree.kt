package com.thoughtworks.archguard.code

class CodeTree {
    var trees: HashMap<String, Node> = HashMap()

    fun addClass(nodeName: String) {
        val split = nodeName.split(".")
        val classTopPackage = split[0]
        if (split.size == 1) {
            trees.put(classTopPackage, Node(classTopPackage, TypeEnum.FILE))
            return
        }
        if (!trees.keys.contains(classTopPackage)) {
            val topNode = Node(classTopPackage, TypeEnum.PACKAGE)
            addOrUpdateNodes(topNode, split)
            trees.put(classTopPackage, topNode)
            return
        }
        val topNode = trees.get(classTopPackage)!!
        addOrUpdateNodes(topNode, split)
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