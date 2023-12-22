package org.archguard.scanner.analyser.sca.gradle

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.tree.nodes.TomlFile
import com.akuleshov7.ktoml.tree.nodes.TomlKeyValuePrimitive
import com.akuleshov7.ktoml.tree.nodes.TomlNode
import com.akuleshov7.ktoml.tree.nodes.TomlTablePrimitive
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

    private val toml = Toml(
        inputConfig = TomlInputConfig(
            ignoreUnknownNames = true,
            allowEmptyValues = true,
            allowNullValues = true,
            allowEscapedQuotesInLiteralStrings = true,
            allowEmptyToml = true,
        )
    )

    fun parse(): MutableMap<String, DependencyEntry> {
        val entries: MutableMap<String, DependencyEntry> = mutableMapOf()
        val tomlFile = try {
            toml.tomlParser.parseString(content)
        } catch (e: Exception) {
            return entries
        }

        val versions: MutableMap<String, String> = mutableMapOf()
        tomlFile.children.filter { it.name == "versions" }.map { node ->
            node.children.forEach {
                when (it) {
                    is TomlKeyValuePrimitive -> {
                        versions[it.name] = it.value.content.toString()
                    }

                    else -> {
                        logger.warn("Unknown node type: ${it.javaClass}")
                    }
                }
            }
        }

        entries += versionCatalogLibraryDependencies(tomlFile, versions)

        return entries
    }


    private fun versionCatalogLibraryDependencies(
        tomlFile: TomlFile,
        versions: MutableMap<String, String>
    ): MutableMap<String, DependencyEntry> {
        val entries: MutableMap<String, DependencyEntry> = mutableMapOf()

        tomlFile.children
            .filter { it.name == "libraries" }
            .forEach { entries += dependenciesForDeclarations(it.children, versions) }

        return entries
    }

    private fun dependenciesForDeclarations(
        declarations: MutableList<TomlNode>,
        versions: MutableMap<String, String>
    ): Map<String, DependencyEntry> {
        return declarations.associate { declaration ->
            val entry = DependencyEntry(name = "", aliasName = declaration.name)
            declaration.children.forEach {
                val value = valueFromToNode(it)
                when (it.name) {
                    "version" -> {
                        val children = it.children
                        if (children.isNotEmpty() && children[0].name == "ref") {
                            val ref = valueFromToNode(children[0])
                            entry.version = versions[ref] ?: ""
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
                        val module = value
                        val parts = module.split(":")
                        entry.group = parts.getOrNull(0) ?: ""
                        entry.artifact = parts.getOrNull(1) ?: ""
                    }

                    else -> {
                        logger.warn("Unknown node type: ${it.javaClass}")
                    }
                }
            }

            if (declaration.children.isEmpty()) {
                val module = valueFromToNode(declaration)
                val parts = module.split(":")
                entry.group = parts.getOrNull(0) ?: ""
                entry.artifact = parts.getOrNull(1) ?: ""
                entry.version = parts.getOrNull(2) ?: ""
            }

            entry.name = "${entry.group}:${entry.artifact}"
            entry.aliasName to entry
        }
    }

    fun valueFromToNode(node: TomlNode): String {
        return when (node) {
            is TomlKeyValuePrimitive -> {
                node.value.content.toString()
            }

            is TomlTablePrimitive -> {
                node.fullTableKey.toString()
            }

            else -> {
                logger.warn("TomlNode toString - Unknown node type: ${node.javaClass}")
                node.toString()
            }
        }
    }
}