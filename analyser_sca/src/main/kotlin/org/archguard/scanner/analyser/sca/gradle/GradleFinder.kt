package org.archguard.scanner.analyser.sca.gradle

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.tree.nodes.TomlFile
import com.akuleshov7.ktoml.tree.nodes.TomlNode
import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.analyser.sca.base.Finder
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.DependencyEntry
import org.archguard.scanner.core.sca.PackageDependencies
import java.io.File

class GradleFinder : Finder() {
    override val parser: Parser = GradleParser()

    val toml = Toml(
        inputConfig = TomlInputConfig(
            ignoreUnknownNames = false,
            allowEmptyValues = true,
            allowNullValues = true,
            allowEscapedQuotesInLiteralStrings = true,
            allowEmptyToml = true,
        )
    )

    override fun isMatch(it: File): Boolean {
        if (it.isDirectory) return false

        return it.name == "build.gradle" || it.name == "build.gradle.kts"
    }

    override fun process(path: String): List<PackageDependencies> {
        val fileTreeWalk = File(path).walk(FileWalkDirection.BOTTOM_UP)

        val versionToml = fileTreeWalk.filter {
            it.canonicalPath.endsWith("gradle/libs.versions.toml")
        }.take(1)

        versionToml.forEach { file ->
            toml.tomlParser.parseString(file.readText()).let { tomlFile ->
                val versions: MutableMap<String, String> = mutableMapOf()
                tomlFile.children.filter { it.name == "versions" }.map { node ->
                    node.children.forEach {
                        versions[it.name] = it.toString()
                    }
                }
                versionCatalogLibraryDependencies(tomlFile, versions)
            }
        }

        return fileTreeWalk
            .filter {
                isMatch(it)
            }
            .flatMap {
                val file = DeclFileTree(filename = it.name, path = it.canonicalPath, content = it.readText())
                parser.lookupSource(file)
            }.toList()
    }


    private fun versionCatalogLibraryDependencies(
        tomlFile: TomlFile,
        versions: MutableMap<String, String>
    ): List<DependencyEntry> {
        return tomlFile.children.filter {
            it.name == "libraries"
        }.map {
            dependenciesForDeclarations(it.children, versions)
        }.flatten()
    }

    private fun dependenciesForDeclarations(
        declarations: MutableList<TomlNode>,
        versions: MutableMap<String, String>
    ): List<DependencyEntry> {
        return declarations.map { declaration ->
            //        return DependencyEntry
            val dependencyEntry = DependencyEntry(name = declaration.name, version = "", group = "", artifact = "")

            declaration.children.forEach {
                if (it.name == "version") {
                    dependencyEntry.version = it.toString()
                }
                if (it.name == "group") {
                    dependencyEntry.group = it.toString()
                }
                if (it.name == "name") {
                    dependencyEntry.artifact = it.toString()
                }
            }

            dependencyEntry
        }
    }
}
