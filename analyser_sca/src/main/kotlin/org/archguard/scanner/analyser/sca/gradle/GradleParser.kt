package org.archguard.scanner.analyser.sca.gradle

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.sca.DEP_SCOPE
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.scanner.core.sca.DependencyEntry
import org.archguard.scanner.analyser.sca.base.Parser

private val GRADLE_SHORT_IMPL_REGEX =
    // implementation "joda-time:joda-time:2.2"
    "([a-zA-Z]+)?(?:\\(|\\s)\\s*['\"](([^\\s,@'\":\\/\\\\]+):([^\\s,@'\":\\/\\\\]+):([^\\s,'\":\\/\\\\]+))['\"]".toRegex()

// gradle version catalog
private val GRADLE_VERSION_CATALOG_REGEX =
    // implementation(libs.bundles.openai)
    "([a-zA-Z]+)(?:\\(|\\s)(([a-zA-Z]+)((\\.[a-zA-Z]+)+))".toRegex()

private val GRADLE_KEYWORD_REGEX =
    // runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
    "\\s+([a-zA-Z]+)(?:^|\\s|,|\\()((([a-zA-Z]+)(\\s*=|:)\\s*['\"]([^'\"]+)['\"][\\s,]*)+)\\)".toRegex()

private val DEPENDENCY_SET_START_REGEX =
    "(?:^|\\s)dependencySet\\(([^\\)]+)\\)\\s*\\{".toRegex()

private val ENTRY_REGEX =
    "\\s+entry\\s+['\"]([^\\s,@'\":\\/\\\\]+)['\"]".toRegex()

class GradleParser : Parser() {
    var versionCatalogs: MutableMap<String, DependencyEntry> = mutableMapOf()

    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        val deps = mutableListOf<DependencyEntry>()

        deps += parseShortForm(file.content)
        deps += parseKeywordArg(file.content)
        deps += parseDependencySet(file.content)
        deps += parseVersionCatalog(file.content)

        return listOf(
            PackageDependencies(
                name = "",
                version = "",
                packageManager = "gradle",
                dependencies = deps,
                path = file.path
            )
        )
    }

    private fun parseVersionCatalog(content: String): List<DependencyEntry> {
        val findAll = GRADLE_VERSION_CATALOG_REGEX.findAll(content)
        return findAll.filter {
            it.groups.isNotEmpty() && it.value.contains("libs.")
        }.map {
            val groups = it.groups

            val matchGroup = groups[2]!!.value
                .substringAfter("libs.")
                .replace(".", "-")

            val dependencyEntry = versionCatalogs[matchGroup]
            dependencyEntry ?: DependencyEntry(
                name = groups[2]!!.value,
                group = "",
                artifact = groups[2]!!.value,
                version = "",
                scope = DEP_SCOPE.from(groups[0]!!.value)
            )
        }.toList()
    }

    // sample: implementation "joda-time:joda-time:2.2"
    private fun parseShortForm(content: String): List<DependencyEntry> {
        return GRADLE_SHORT_IMPL_REGEX.findAll(content).filter {
            it.groups.isNotEmpty() && it.groups.size == 6
        }.map {
            val groups = it.groups
            val scope = scopeForGradle(groups[1]?.value ?: "")
            DependencyEntry(
                name = "${groups[3]!!.value}:${groups[4]!!.value}",
                group = groups[3]!!.value,
                artifact = groups[4]!!.value,
                version = groups[5]!!.value,
                scope = scope
            )
        }.toList()
    }

    // sample: `testRuntimeOnly`
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
    private fun parseKeywordArg(content: String): List<DependencyEntry> {
        return content.lines().mapNotNull { line ->
            val matchResult = GRADLE_KEYWORD_REGEX.find(line)
            if (matchResult != null && matchResult.groups.size > 6) {
                val scope = scopeForGradle(matchResult.groups[1]!!.value)
                val group = valueFromRegex("group", line)
                val artifact = valueFromRegex("name", line)
                val version = valueFromRegex("version", line)

                DependencyEntry(
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

    // case 1: `name = 'joda-time'`
    // case 2: `name: 'joda-time'`
    private fun valueFromRegex(key: String, text: String): String {
        val keyRegex = "($key(\\s*=|:)\\s*['\"]([^'\"]+)['\"])".toRegex()
        val matchResult = keyRegex.find(text)
        if (matchResult != null) {
            return matchResult.groups[3]!!.value
        }
        return ""
    }

    // sample: dependencySet(group:'org.slf4j', version: '1.7.7') { entry 'slf4j-api' }
    private fun parseDependencySet(content: String): List<DependencyEntry> {
        val deps = mutableListOf<DependencyEntry>()
        val sequence = content.lineSequence().iterator()
        while (sequence.hasNext()) {
            val text = sequence.next()
            val depSetResult = DEPENDENCY_SET_START_REGEX.find(text)
            if (depSetResult != null && depSetResult.groups.size == 2) {
                val entries: MutableList<String> = mutableListOf()
                val entry = depSetResult.groups[1]!!.value
                val group = valueFromRegex("group", entry)
                val version = valueFromRegex("version", entry)

                while (sequence.hasNext()) {
                    val next = sequence.next()
                    if (next.contains("}")) {
                        break
                    } else {
                        entries += next
                    }
                }

                entries.map {
                    val find = ENTRY_REGEX.find(it)
                    if (find != null && find.groups.size == 2) {
                        val artifact = find.groups[1]!!.value

                        deps += DependencyEntry(
                            name = "$group:$artifact",
                            artifact = artifact,
                            group = group,
                            version = version,
                        )

                    }
                }
            }
        }

        return deps
    }

    // sample: plugins { }
    private fun parsePlugin() {}
}
