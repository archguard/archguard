package org.archguard.scanner.analyser.sca.gradle

import chapi.domain.core.CodeContainer
import chapi.domain.core.CodeField
import chapi.parser.toml.TomlAnalyser
import org.archguard.scanner.core.sca.DependencyEntry


/**
 * This class parses the gradle/libs.versions.toml file and returns a list of DependencyEntry objects.

 * Example:
 * ```toml
 * [versions]
 * groovy = "3.0.5"
 * checkstyle = "8.37"
 *
 * [libraries]
 * groovy-core = { module = "org.codehaus.groovy:groovy", version.ref = "groovy" }
 * groovy-json = { module = "org.codehaus.groovy:groovy-json", version.ref = "groovy" }
 * groovy-nio = { module = "org.codehaus.groovy:groovy-nio", version.ref = "groovy" }
 *
 * [bundles]
 * groovy = ["groovy-core", "groovy-json", "groovy-nio"]
 *
 * [plugins]
 * versions = { id = "com.github.ben-manes.versions", version = "0.45.0" }
 * ```
 */
class GradleTomlParser(private val content: String) {
    private val logger = mu.KotlinLogging.logger {}
    val analyser = TomlAnalyser()

    fun parse(): MutableMap<String, DependencyEntry> {
        val entries: MutableMap<String, DependencyEntry> = mutableMapOf()
        val tomlFile = try {
            val containers = analyser.analysis(content, "libs.versions.toml")
            containers
        } catch (e: Exception) {
            return entries
        }

        val versions: MutableMap<String, String> = mutableMapOf()
        tomlFile.Containers.filter { it.PackageName == "versions" }.map { container ->
            container.Fields.forEach {
                versions[it.TypeKey] = it.TypeValue
            }
        }

        entries += versionCatalogLibraryDependencies(tomlFile, versions)

        return entries
    }


    private fun versionCatalogLibraryDependencies(
        tomlFile: CodeContainer,
        versions: MutableMap<String, String>
    ): MutableMap<String, DependencyEntry> {
        val entries: MutableMap<String, DependencyEntry> = mutableMapOf()

        tomlFile.Containers
            .filter { it.PackageName == "libraries" }
            .forEach { entries += dependenciesForDeclarations(it.Fields, versions) }

        return entries
    }

    private fun dependenciesForDeclarations(
        declarations: List<CodeField>,
        versions: MutableMap<String, String>
    ): Map<String, DependencyEntry> {
        return declarations.associate { declaration ->
            val entry = DependencyEntry(name = "", aliasName = declaration.TypeKey)
            declaration.ArrayValue.forEach {
                val value = it.TypeValue
                when (it.TypeKey) {
                    "version" -> {
                        val children = it.ArrayValue
                        if (children.isNotEmpty() && children[0].TypeKey == "ref") {
                            entry.version = children[0].TypeValue ?: ""
                        } else {
                            entry.version = value
                        }
                    }

                    "version.ref" -> {
                        entry.version = versions[value] ?: ""
                    }

                    "group" -> {
                        entry.group = value
                    }

                    "name" -> {
                        entry.artifact = value
                    }

                    "module" -> {
                        val parts = value.split(":")
                        entry.group = parts.getOrNull(0) ?: ""
                        entry.artifact = parts.getOrNull(1) ?: ""
                    }

                    else -> {
                        logger.warn("Unknown node type: ${it.javaClass}")
                    }
                }
            }

            if (declaration.TypeType == "String") {
                val module = declaration.TypeValue
                val parts = module.split(":")
                entry.group = parts.getOrNull(0) ?: ""
                entry.artifact = parts.getOrNull(1) ?: ""
                entry.version = parts.getOrNull(2) ?: ""
            }

            entry.name = "${entry.group}:${entry.artifact}"
            entry.aliasName to entry
        }
    }
}