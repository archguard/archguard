package org.archguard.analyser.sca.processor

import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.analyser.sca.parser.Parser
import java.io.File

abstract class ScaFinder {
    abstract val parser: Parser

    open fun isMatch(it: File): Boolean {
        return false
    }

    open fun process(path: String): List<PackageDependencies> {
        return File(path).walk(FileWalkDirection.BOTTOM_UP)
            .filter {
                isMatch(it)
            }
            .flatMap {
                val file = DeclFileTree(filename = it.name, path = it.canonicalPath, content = it.readText())
                parser.lookupSource(file)
            }.toList()
    }
}
