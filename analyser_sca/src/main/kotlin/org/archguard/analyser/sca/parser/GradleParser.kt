package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.common.Finder
import org.archguard.analyser.sca.model.DEP_SCOPE
import org.archguard.analyser.sca.model.DeclFile
import org.archguard.analyser.sca.model.DepDecl
import org.archguard.analyser.sca.model.DepDependency

private val GRADLE_SHORT_IMPL_REGEX =
    // implementation "joda-time:joda-time:2.2"
    "([a-zA-Z]+)(?:\\(|\\s)\\s*['\"](([^\\s,@'\":\\/\\\\]+):([^\\s,@'\":\\/\\\\]+):([^\\s,'\":\\/\\\\]+))['\"]".toRegex()

private val GRADLE_KEYWORD_REGEX =
    // runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
    "\\s+([a-zA-Z]+)(?:^|\\s|,|\\()((([a-zA-Z]+)(\\s*=|:)\\s*['\"]([^'\"]+)['\"][\\s,]*)+)\\)".toRegex()

private val DEPENDENCY_SET_START_REGEX =
    "(?:^|\\s)dependencySet\\(([^\\)]+)\\)\\s*\\{".toRegex()

class GradleParser : Finder() {
    override fun lookupSource(file: DeclFile): List<DepDecl> {
        val deps = mutableListOf<DepDependency>()
        deps += parseShortform(file.content)
        deps += parseKeywordArg(file.content)
        deps += parseDependencySet(file.content)

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
        return GRADLE_SHORT_IMPL_REGEX.findAll(content).filter {
            it.groups.isNotEmpty() && it.groups.size == 6
        }.map {
            val groups = it.groups
            val scope = scopeForGradle(groups[1]!!.value)
            DepDependency(
                name = "${groups[3]!!.value}:${groups[4]!!.value}",
                artifact = groups[3]!!.value,
                group = groups[4]!!.value,
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
    private fun parseKeywordArg(content: String): List<DepDependency> {
        return content.lines().mapNotNull { line ->
            val matchResult = GRADLE_KEYWORD_REGEX.find(line)
            if (matchResult != null && matchResult.groups.size > 6) {
                val scope = scopeForGradle(matchResult.groups[1]!!.value)
                val group = valueFromRegex("group", line)
                val artifact = valueFromRegex("name", line)
                val version = valueFromRegex("version", line)

                DepDependency(
                    name = "$group:$artifact",
                    group = group,
                    artifact = artifact,
                    version = version,
                    scope = scope
                )
            } else {
                null
            }
        }
    }

    private fun valueFromRegex(key: String, text: String): String {
        val keyRegex = "($key(\\s*=|:)\\s*['\"]([^'\"]+)['\"])".toRegex()
        val matchResult = keyRegex.find(text)
        if (matchResult != null) {
            return matchResult.groups[3]!!.value
        }
        return ""
    }

    // sample: dependencySet(group:'org.slf4j', version: '1.7.7') { entry 'slf4j-api' }
    private fun parseDependencySet(content: String): List<DepDependency> {
        return content.lines().mapNotNull {
            val matchResult = DEPENDENCY_SET_START_REGEX.find(it)
            if(matchResult != null && matchResult.groups.size == 2) {
                val entry = matchResult.groups[1]!!.value
                val group = valueFromRegex("group", entry)
                val version = valueFromRegex("version", entry)

                DepDependency (
                    name = "",
                    artifact = "",
                    group = group,
                    version = version,
                )
            } else {
                null
            }
        }.toList()
    }

    // sample: plugins { }
    private fun parsePlugin() {}
}