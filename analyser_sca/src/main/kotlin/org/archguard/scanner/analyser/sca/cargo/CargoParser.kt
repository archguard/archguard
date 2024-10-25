package org.archguard.scanner.analyser.sca.cargo

import chapi.domain.core.CodeField
import chapi.parser.toml.TomlAnalyser
import chapi.parser.toml.TomlType
import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.context.DEP_SCOPE
import org.archguard.context.DeclFileTree
import org.archguard.context.DependencyEntry
import org.archguard.context.PackageDependencies

class CargoParser : Parser() {
    val analyser = TomlAnalyser()

    /**
     *
     * example [DeclFileTree.content] for Cargo.toml:
     *
     * ```toml
     * [dependencies]
     * ort = { version = "2.0.0-alpha.1", default-features = true }
     * tokenizers = { version = "0.15.0", default-features = false, features = ["progressbar", "cli", "onig", "esaxx_fast"] }
     * ndarray = "0.15.6"
     * enfer_core = { path = "../enfer_core" }
     *
     * [build-dependencies]
     * uniffi = { version = "0.25", features = ["build"] }
     *```
     */
    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        val containers = analyser.analysis(file.content, file.path).Containers
        val dependencies: List<DependencyEntry> =
            containers.filter { it.PackageName.endsWith("dependencies") }.map { container ->
                val scope = DEP_SCOPE.rust(container.PackageName)
                container.Fields.map {
                    val name = it.TypeKey
                    val version = lookupVersion(it)

                    DependencyEntry(
                        name,
                        group = "",
                        artifact = name,
                        version = version,
                        scope,
                    )
                }
            }.flatten()

        if (dependencies.isEmpty()) {
            return listOf()
        }

        return listOf(
            PackageDependencies(file.name, "", "cargo", dependencies, file.path)
        )
    }

    private fun lookupVersion(field: CodeField): String {
        return when (TomlType.fromString(field.TypeType)) {
            TomlType.InlineTable -> {
                val version = lookupByFieldChild(field.ArrayValue, "version") ?: field.TypeValue
                version.ifEmpty { field.TypeValue }
            }

            else -> field.TypeValue
        }
    }

    private fun lookupByFieldChild(arrayValue: List<CodeField>, propName: String): String? {
        return arrayValue.firstOrNull { it.TypeKey == propName }?.TypeValue
    }
}

private fun DEP_SCOPE.Companion.rust(tableName: String): DEP_SCOPE {
    if (tableName.contains("target.")) return DEP_SCOPE.OPTIONAL

    return when(tableName) {
        "dependencies" -> DEP_SCOPE.NORMAL
        "build-dependencies" -> DEP_SCOPE.BUILD
        "dev-dependencies" -> DEP_SCOPE.DEV
        else -> DEP_SCOPE.NORMAL
    }
}
