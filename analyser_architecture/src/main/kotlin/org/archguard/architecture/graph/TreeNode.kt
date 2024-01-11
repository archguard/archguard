package org.archguard.architecture.graph

import kotlinx.serialization.Serializable

/**
 * The `TreeNode` class represents a node in a package tree. Each node has a name and a list of children nodes.
 *
 * @property name The name of the node.
 * @property children The list of children nodes.
 *
 * @constructor Creates a new `TreeNode` with the specified name and children.
 *
 * @param name The name of the node.
 * @param children The list of children nodes. Defaults to an empty list.
 *
 * @see create
 * @see findOrCreateChild
 * @see printPackageTree
 */
@Serializable
data class TreeNode(val name: String, val children: MutableList<TreeNode> = mutableListOf()) {
    companion object {
        /**
         * Builds a package tree based on the given list of package names.
         *
         * @param packageNames the list of package names to build the tree from,
         * e.g. `["org.archguard", "org.archguard.architecture", "org.archguard.backend"]`.
         * @return the root node of the package tree
         *
         * for example:
         * ```kotlin
         * val packageNames = listOf("org.archguard", "org.archguard.architecture", "org.archguard.backend")
         * val rootNode = TreeNode.create(packageNames)
         * ```
         */
        fun create(packageNames: List<String>): TreeNode {
            val root = TreeNode("root")

            for (packageName in packageNames) {
                val parts = packageName.split(".")
                var currentNode = root

                for (part in parts) {
                    currentNode = findOrCreateChild(currentNode, part)
                }
            }

            return root
        }


        /**
         * Finds or creates a child node with the given name in the specified parent node.
         *
         * @param parent The parent node in which to search for the child node.
         * @param childName The name of the child node to find or create.
         * @return The found or newly created child node.
         */
        fun findOrCreateChild(parent: TreeNode, childName: String): TreeNode {
            parent.children.find { child -> child.name == childName }?.let { return it }

            val newChild = TreeNode(childName)
            parent.children.add(newChild)
            return newChild
        }

        /**
         * Prints the package tree starting from the given node.
         *
         * This method recursively prints the package tree structure starting from the specified node. Each package name is
         * indented based on its level in the tree.
         *
         * @param node The root node of the package tree.
         * @param indent The indentation string to be used for each level of the package tree. Defaults to an empty string.
         *
         * @see TreeNode
         */
        fun printPackageTree(node: TreeNode, indent: String = "") {
            println(indent + node.name)
            for (child in node.children) {
                printPackageTree(child, "$indent  ")
            }
        }
    }
}
