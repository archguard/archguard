package org.archguard.analyser.sca

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDecl
import org.archguard.analyser.sca.parser.GradleParser
import java.io.File

class JavaFinder {
    fun getDeclTree(path: String): List<DepDecl> {
        return File(path).walk(FileWalkDirection.BOTTOM_UP)
            .filter {
                it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"
            }
            .flatMap {
                val file = DeclFileTree(filename = it.name, path = it.absolutePath, content = it.readText())
                GradleParser().lookupSource(file)
            }.toList()
    }

    fun buildDeclTree(path: String): DeclFileTree? {
        val dirPaths = File(path).walk(FileWalkDirection.BOTTOM_UP)
            .filter {
                it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"
            }.map {
                it.absolutePath
            }
            .sortedBy {
                it.split(File.separatorChar).size
            }
            .toMutableList()

        val nodesFromPathList = nodesFromPathList(dirPaths)
        return nodesFromPathList
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

            try {
                nodeMap[parent]!!.childrens += it.value
            } catch (e: NullPointerException) {
//                println("Parent not found")
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