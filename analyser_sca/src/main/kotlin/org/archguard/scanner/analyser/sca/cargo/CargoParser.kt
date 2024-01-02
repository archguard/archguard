package org.archguard.scanner.analyser.sca.cargo

import com.akuleshov7.ktoml.tree.nodes.TomlFile
import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.analyser.sca.base.createTomlParser
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.PackageDependencies

class CargoParser : Parser() {
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
        val tomlFile: TomlFile = createTomlParser().parseString(file.content)
//        val tomlNodes = tomlFile.children.filter { it.name.endsWith("dependencies") }
//        tomlNodes.map { node ->
//            node.children.forEach {
//               println(it)
//            }
//        }

        return listOf()
    }
}