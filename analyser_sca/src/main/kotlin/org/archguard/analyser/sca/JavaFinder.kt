package org.archguard.analyser.sca

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDecl
import org.archguard.analyser.sca.parser.GradleParser
import java.io.File

class JavaFinder {
    fun getDeclTree(path: String): List<DepDecl> {
        return File(path).walk()
            .filter {
                it.isFile && it.name == "build.gradle" || it.name == "build.gradle.kts"
            }
            .flatMap {
                val file = DeclFileTree(name = it.name, path = it.absolutePath, content = it.readText())
                GradleParser().lookupSource(file)
            }.toList()
    }
}