package org.archguard.analyser.sca

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDeclaration
import org.archguard.analyser.sca.parser.NpmParser
import java.io.File

class JavaScriptFinder {
    private fun isPackageJson(it: File) = it.isFile && it.name == "package.json"

    fun find(path: String): List<DepDeclaration> {
        return byPackageJson(path).toMutableList()
    }

    private fun byPackageJson(path: String): List<DepDeclaration> {
        return File(path).walk(FileWalkDirection.BOTTOM_UP)
            .filter {
                isPackageJson(it)
            }
            .flatMap {
                val file = DeclFileTree(filename = it.name, path = it.canonicalPath, content = it.readText())
                NpmParser().lookupSource(file)
            }.toList()
    }
}
