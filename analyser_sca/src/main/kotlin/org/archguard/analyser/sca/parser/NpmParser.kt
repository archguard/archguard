package org.archguard.analyser.sca.parser

import com.jayway.jsonpath.JsonPath
import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.DepDeclaration
import org.archguard.analyser.sca.model.DepDependency

class NpmParser : Parser() {
    override fun lookupSource(file: DeclFileTree): List<DepDeclaration> {
        val name: String = JsonPath.read(file.content, "$.name")
        val version: String = JsonPath.read(file.content, "$.version")
        val depMap: Map<String, String> = JsonPath.read(file.content, "$.dependencies")

        return listOf(DepDeclaration(
            name,
            version,
            "npm",
            this.createDependencies(depMap)
        ))
    }

    private fun createDependencies(depMap: Map<String, String>): List<DepDependency> {
        return depMap.map {
            DepDependency(
                name = it.key,
                group = "",
                artifact = it.key,
                version = it.value
            )
        }.toList()
    }
}