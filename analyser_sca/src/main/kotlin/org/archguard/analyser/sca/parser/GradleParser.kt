package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.common.Finder
import org.archguard.analyser.sca.model.DEP_SCOPE
import org.archguard.analyser.sca.model.DeclFile
import org.archguard.analyser.sca.model.DepDecl
import org.archguard.analyser.sca.model.DepDependency

private val GRADLE_IMPL_REGEX =
    "([a-zA-Z]+)(?:\\(|\\s)\\s*['\"](([^\\s,@'\":\\/\\\\]+):([^\\s,@'\":\\/\\\\]+):([^\\s,'\":\\/\\\\]+))['\"]".toRegex()

class GradleParser : Finder() {
    override fun lookupSource(file: DeclFile): List<DepDecl> {
        val deps = mutableListOf<DepDependency>()
        deps += parseShortform(file.content)

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
            it.groups.isNotEmpty() && it.groups.size == 6
        }.map {
            val groups = it.groups
            val scope = scopeForGradle(groups[1]!!.value)
            DepDependency(
                name = "${groups[3]!!.value}:${groups[4]!!.value}",
                artifact = groups[3]!!.value,
                group = listOf(groups[4]!!.value),
                version = groups[5]!!.value,
                scope = scope
            )
        }.toList()
    }

    private fun scopeForGradle(text: String): DEP_SCOPE {
        if (text.startsWith("test")) {
            return DEP_SCOPE.TEST
        }

        if (text.startsWith("runtime")) {
            return DEP_SCOPE.RUNTIME
        }

        return DEP_SCOPE.NORMAL
    }

    // sample: runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
    private fun parseKeywordArg() {}

    // sample: dependencySet(group:'org.slf4j', version: '1.7.7') { entry 'slf4j-api' }
    private fun parseDependencySet() {}

    // sample: plugins { }
    private fun parsePlugin() {}
}