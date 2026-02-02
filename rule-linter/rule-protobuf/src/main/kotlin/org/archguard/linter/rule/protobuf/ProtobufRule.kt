package org.archguard.linter.rule.protobuf

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.archguard.rule.core.IssueEmit
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleContext
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Base rule for protobuf linting.
 *
 * We intentionally model the visit around a single `.proto` file to make it easy to:
 * - implement file-level rules (file name, package, imports ordering, etc.)
 * - implement service/rpc/message/field rules from parsed [CodeDataStruct]s
 * - allow rule implementations to consult raw file content when needed
 */
open class ProtobufRule : Rule() {
    open fun visitFile(
        filePath: String,
        structsInFile: List<CodeDataStruct>,
        context: ProtobufRuleContext,
        callback: IssueEmit,
    ) {
        // default: do nothing
    }

    protected fun emit(callback: IssueEmit, position: IssuePosition = IssuePosition()) {
        callback(this, position)
    }
}

class ProtobufRuleContext(
    val config: ProtobufLintConfig,
) : RuleContext() {
    private val linesCache: MutableMap<String, List<String>> = mutableMapOf()

    fun fileLines(filePath: String): List<String> {
        return linesCache.getOrPut(filePath) {
            runCatching { File(filePath).readText().lines() }.getOrDefault(emptyList())
        }
    }
}

@Serializable
data class ProtobufLintConfig(
    val enabledRuleIds: Set<String> = emptySet(),
    val disabledRuleIds: Set<String> = emptySet(),
    val ignore: Map<String, IgnoreRule> = emptyMap(),
) {
    fun isRuleEnabled(ruleId: String): Boolean {
        if (disabledRuleIds.contains(ruleId)) return false
        if (enabledRuleIds.isEmpty()) return true
        return enabledRuleIds.contains(ruleId)
    }

    fun isIgnored(ruleId: String, filePath: String): Boolean {
        val cfg = ignore[ruleId] ?: return false
        return cfg.pathContains.any { filePath.contains(it) } ||
            cfg.pathRegex.any { runCatching { Regex(it).containsMatchIn(filePath) }.getOrDefault(false) }
    }

    @Serializable
    data class IgnoreRule(
        val pathContains: List<String> = emptyList(),
        val pathRegex: List<String> = emptyList(),
    )
}

object ProtobufLintConfigLoader {
    private const val CONFIG_FILE = ".archguard-protobuf.json"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun load(startDir: Path = Paths.get(System.getProperty("user.dir"))): ProtobufLintConfig {
        findConfigFile(startDir)?.let { file ->
            return runCatching {
                json.decodeFromString(ProtobufLintConfig.serializer(), file.readText())
            }.getOrDefault(ProtobufLintConfig())
        }

        return ProtobufLintConfig()
    }

    private fun findConfigFile(startDir: Path): File? {
        var dir: Path? = startDir.toAbsolutePath()
        while (dir != null) {
            val candidate = dir.resolve(CONFIG_FILE).toFile()
            if (candidate.exists() && candidate.isFile) return candidate
            dir = dir.parent
        }
        return null
    }
}
