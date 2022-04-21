package org.archguard.scanner.dep.parser

import org.archguard.scanner.dep.common.Finder
import org.archguard.scanner.dep.model.DeclFile
import org.archguard.scanner.dep.model.DepDecl
import org.archguard.scanner.dep.model.DepDependency

private val GRADLE_IMPL_REGEX =
    "(?:\\(|\\s)\\s*['\"](([^\\s,@'\":\\/\\\\]+):([^\\s,@'\":\\/\\\\]+):([^\\s,'\":\\/\\\\]+))['\"]".toRegex()

class GradleParser : Finder() {
    override fun lookupSource(file: DeclFile): List<DepDecl> {
        val deps = parseShortform(file.content)
        return listOf(
            DepDecl(
                name = "",
                version = "",
                packageManager = "gradle",
                dependencies = deps
            )
        )
    }

    // sample: implementation "joda-time:joda-time:2.2"
    private fun parseShortform(content: String): List<DepDependency> {
        return GRADLE_IMPL_REGEX.findAll(content).filter {
            it.groups.isNotEmpty() && it.groups.size == 5
        }.map {
            val groups = it.groups
            DepDependency(
                name = "${groups[2]!!.value}:${groups[3]!!.value}",
                artifact = groups[2]!!.value,
                group = listOf(groups[3]!!.value),
                version = groups[4]!!.value
            )
        }.toList()
    }

    // sample: runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
    private fun parseKeywordArg() {}

    // sample: dependencySet(group:'org.slf4j', version: '1.7.7') { entry 'slf4j-api' }
    private fun parseDependencySet() {}

    // sample: plugins { }
    private fun parsePlugin() {}
}