package com.thoughtworks.archguard.codetree

class Node(var node: String, var type: TypeEnum) {
    var children: HashSet<Node> = HashSet()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (node != other.node) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = node.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }


}

enum class TypeEnum {
    SUB_MODULE, PACKAGE, FILE
}
