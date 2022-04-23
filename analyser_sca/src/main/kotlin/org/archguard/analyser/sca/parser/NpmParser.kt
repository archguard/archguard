package org.archguard.analyser.sca.parser

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.JsonPath.using
import com.jayway.jsonpath.Option
import org.archguard.analyser.sca.model.DEP_SCOPE
import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDeclaration
import org.archguard.analyser.sca.model.DepDependency

class NpmParser : Parser() {
    val conf: Configuration = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build()
    var depTypeMap: Map<String, DEP_SCOPE> = mapOf(
        "dependencies" to DEP_SCOPE.NORMAL,
        "optionalDependencies" to DEP_SCOPE.OPTIONAL,
        "devDependencies" to DEP_SCOPE.DEV
    )

    override fun lookupSource(file: DeclFileTree): List<DepDeclaration> {
        val name: String = JsonPath.read(file.content, "$.name")
        val version: String = JsonPath.read(file.content, "$.version")

        val deps = listOf("dependencies", "optionalDependencies", "devDependencies").flatMap {
            createDepByType(it, file.content)
        }.toList()

        return listOf(DepDeclaration(
            name,
            version,
            "npm",
            deps
        ))
    }

    private fun createDepByType(
        field: String,
        content: String,
    ): List<DepDependency> {
        val listOf = mutableListOf<DepDependency>()
        val optionalDep: Map<String, String>? = using(conf).parse(content).read("$.$field");
        if (optionalDep != null) {
            listOf += createDependencies(optionalDep, depTypeMap[field]!!)
        }

        return listOf
    }

    private fun createDependencies(depMap: Map<String, String>, scope: DEP_SCOPE): List<DepDependency> {
        return depMap.map {
            DepDependency(
                name = it.key,
                group = "",
                artifact = it.key,
                version = it.value,
                scope = scope
            )
        }.toList()
    }
}