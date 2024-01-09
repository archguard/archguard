package org.archguard.graph

data class TypedNode(var node: String, var type: TypeEnum) {
    var children: HashSet<TypedNode> = HashSet()
}

enum class TypeEnum {
    SUB_MODULE, PACKAGE, FILE
}
