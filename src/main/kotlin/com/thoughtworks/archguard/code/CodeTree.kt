package com.thoughtworks.archguard.code

class CodeTree {
    var trees: HashMap<String, Node> = HashMap()

    fun addClass(nodeName: String) {
        // TODO 顶层文件
        val split = nodeName.split(".")
        val classTopPackage = split[0]
        if (!trees.keys.contains(classTopPackage)) {
            val topNode = Node(classTopPackage, TypeEnum.PACKAGE)
            var currentNode: Node = topNode
            for (node in split.subList(1, split.size - 1)) {
                val nextNode = Node(node, TypeEnum.PACKAGE)
                currentNode.children.add(nextNode)
                currentNode = currentNode.children.find { it.node == node }!!
            }
            val leafNode = Node(split[split.size - 1], TypeEnum.FILE)
            currentNode.children.add(leafNode)
            trees.put(classTopPackage, topNode)
        } else {
            val topNode = trees.get(classTopPackage)!!
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
}