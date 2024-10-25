package org.archguard.scanner.analyser.sca.npm

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.JsonPath.using
import com.jayway.jsonpath.Option
import org.archguard.context.DEP_SCOPE
import org.archguard.context.DeclFileTree
import org.archguard.context.PackageDependencies
import org.archguard.context.DependencyEntry
import org.archguard.scanner.analyser.sca.base.Parser

class NpmParser : Parser() {
    val conf: Configuration = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build()
    var depTypeMap: Map<String, DEP_SCOPE> = mapOf(
        "dependencies" to DEP_SCOPE.NORMAL,
        "optionalDependencies" to DEP_SCOPE.OPTIONAL,
        "devDependencies" to DEP_SCOPE.DEV
    )

    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        val name: String = JsonPath.read(file.content, "$.name")
        val version: String = JsonPath.read(file.content, "$.version")

        val deps = listOf("dependencies", "optionalDependencies", "devDependencies").flatMap {
            createDepByType(it, file.content)
        }.toList()

        return listOf(
            PackageDependencies(
                name,
                version,
                "npm",
                deps,
                path = file.path
            )
        )
    }

    private fun createDepByType(
        field: String,
        content: String,
    ): List<DependencyEntry> {
        val listOf = mutableListOf<DependencyEntry>()
        val optionalDep: Map<String, String>? = using(conf).parse(content).read("$.$field");
        if (optionalDep != null) {
            listOf += createDependencies(optionalDep, depTypeMap[field]!!)
        }

        return listOf
    }

    private fun createDependencies(depMap: Map<String, String>, scope: DEP_SCOPE): List<DependencyEntry> {
        return depMap.map {
            DependencyEntry(
                name = it.key,
                group = "",
                artifact = it.key,
                version = it.value,
                scope = scope
            )
        }.toList()
    }
}
