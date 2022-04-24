package org.archguard.scanner.ctl.command

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.client.ArchGuardHttpClient
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs

// this class is the 'controller' of the scanner
// put all input parameters in and build the context out
data class ScannerCommand(
    val type: AnalyserType,
    val systemId: String,
    val serverUrl: String,

    // TODO support git url as well?? move 'git clone' logic in cli
    val path: String,

    // for source code analysing, may be Map, String or AnalyserSpec
    val language: Any? = null,
    val features: List<Any> = emptyList(),
    val withoutStorage: Boolean = false,
) {
    private val languageSpec: AnalyserSpec? by lazy { language?.let { parseIdentifierOrSpec(it) } }
    private val featureSpecs: List<AnalyserSpec> by lazy { features.map { parseIdentifierOrSpec(it) } }

    private fun parseIdentifierOrSpec(identifier: Any) = when (identifier) {
        is AnalyserSpec -> identifier
        is Map<*, *> -> Json.decodeFromJsonElement(Json.encodeToJsonElement(identifier))
        is String ->
            if (identifier.startsWith("{")) Json.decodeFromString(identifier)
            else OfficialAnalyserSpecs.specs().first { it.identifier == identifier.toString().lowercase() }
        else -> throw IllegalArgumentException("Unknown identifier: ${identifier.javaClass}")
    }

    fun buildSourceCodeContext(): CliSourceCodeContext {
        assert(type == AnalyserType.SOURCE_CODE)

        return CliSourceCodeContext(
            language = languageSpec!!.identifier,
            features = featureSpecs.map { it.identifier },
            client = ArchGuardHttpClient(languageSpec!!.identifier, serverUrl, systemId, path),
            path = path,
            withoutStorage = withoutStorage,
            systemId = systemId,
        )
    }

    fun getAnalyserSpecs(): List<AnalyserSpec> = listOfNotNull(languageSpec) + featureSpecs
}
