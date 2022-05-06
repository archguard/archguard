package org.archguard.analyser.sca.processor

import org.archguard.analyser.sca.model.DeclFileTree
import java.io.File

class JavaFinder {
    private fun isGradleFile(it: File) = it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"

    fun buildDeclTree(path: String): DeclFileTree? {
        val dirPaths = File(path).walk(FileWalkDirection.BOTTOM_UP)
            .filter {
                isGradleFile(it)
            }.map {
                it.canonicalPath
            }
            .sortedBy {
                it.split(File.separatorChar).size
            }
            .toMutableList()

        return nodesFromPathList(dirPaths)
    }

    private fun nodesFromPathList(dirPaths: MutableList<String>): DeclFileTree? {
        var ret: DeclFileTree? = null

        val nodeMap = mutableMapOf<String, DeclFileTree>()

        var isFirst = true
        for (dir in dirPaths) {
            val newNode = DeclFileTree(nodeName(dir), dir, File(dir).readText())
            if (isFirst) {
                isFirst = false
                ret = newNode
            }

            nodeMap[dir] = newNode
        }

        nodeMap.forEach {
            val fileName = it.value.filename
            val parent = parentPath(it.key) + fileName

            if (nodeMap[parent] != null) {
                nodeMap[parent]!!.childrens += it.value
            } else {
                // if not parent, set to root
                ret!!.childrens += it.value
            }
        }

        return ret
    }

    private fun nodeName(path: String): String {
        return path.split(File.separator).last()
    }

    private fun parentPath(path: String): String {
        val split = path.split(File.separator)
        var ret = ""
        for (i in 0..split.size - 3) {
            ret += split[i] + File.separator
        }

        return ret

    }
}