package org.archguard.scanner.analyser.sca.gomod

import org.archguard.scanner.analyser.sca.base.Parser
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.DependencyEntry
import org.archguard.scanner.core.sca.PackageDependencies


class GoModParser : Parser() {
    // parse for: github.com/AlekSi/pointer v1.1.0
    private val goDependenceRegex: Regex = Regex("""([a-zA-Z0-9\.\-\/]+)\s+v([a-zA-Z0-9\.\-\/]+)""")

    private val goVersionRegex: Regex = Regex("""go\s+([0-9\.]+)""")

    private val GOMOD = "gomod"

    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        var moduleName = ""
        var goVersion = ""
        val deps = mutableListOf<DependencyEntry>()

        file.content.lines().forEach {
            // parse module name with regex, example: module github.com/marmotedu/iam
            if (it.startsWith("module")) {
                moduleName = it.split(" ")[1]
            }

            // parse go version with regex, example: go 1.14
            if (it.startsWith("go")) {
                goVersion = goVersionRegex.find(it)?.groupValues?.get(1) ?: ""
            }

            // parse dependencies with regex, example: github.com/AlekSi/pointer v1.1.0
            val matchResult = goDependenceRegex.find(it)
            if (matchResult != null) {
                val (name, version) = matchResult.destructured

                // split "github.com/AlekSi/pointer" to { artifact: "github.comAlekSi", group: "pointer" }
                val split = name.split("/")

                val artifact = split.last()
                val group = split.dropLast(1).joinToString("/")

                deps += DependencyEntry(name, group, artifact, version)
            }
        }

        return listOf(PackageDependencies(moduleName, goVersion, GOMOD, deps, file.path))
    }
}
