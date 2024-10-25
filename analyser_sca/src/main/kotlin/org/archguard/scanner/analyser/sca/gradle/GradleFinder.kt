package org.archguard.scanner.analyser.sca.gradle

import org.archguard.scanner.analyser.sca.base.Finder
import org.archguard.model.DeclFileTree
import org.archguard.model.PackageDependencies
import java.io.File

class GradleFinder : Finder() {
    override val parser: GradleParser = GradleParser()

    override fun isMatch(it: File): Boolean {
        if (it.isDirectory) return false

        return it.name == "build.gradle" || it.name == "build.gradle.kts"
    }

    override fun process(path: String): List<PackageDependencies> {
        val fileTreeWalk = File(path).walk(FileWalkDirection.BOTTOM_UP)


        val entryMutableMap = fileTreeWalk
            .firstOrNull {
                it.canonicalPath.endsWith("gradle/libs.versions.toml")
            }
            ?.readText()
            ?.let { GradleTomlParser(it).parse() }
            ?: mutableMapOf()

        parser.versionCatalogs = entryMutableMap

        return fileTreeWalk
            .filter {
                isMatch(it)
            }
            .flatMap {
                val file = DeclFileTree(filename = it.name, path = it.canonicalPath, content = it.readText())
                parser.lookupSource(file)
            }.toList()
    }
}
