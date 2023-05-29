package org.archguard.architecture.detect

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

typealias Language = String

@Serializable
class FrameworkMarkup(
    val language: String,
    // todo: change to knowledge graph
    @SerialName("core_stacks")
    val coreStacks: List<String> = listOf(),
    @SerialName("app_type_mapping")
    val appTypeMapping: HashMap<String, List<String>> = hashMapOf(),
    @SerialName("protocol_mapping")
    var protocolMapping: HashMap<String, List<String>> = hashMapOf(),
    val extends: String = "",
) {
    val depAppTypeMap: HashMap<String, String> = hashMapOf()
    val depProtocolMap: HashMap<String, String> = hashMapOf()

    init {
        appTypeMapping.forEach { appType ->
            appType.value.forEach {
                depAppTypeMap[it] = appType.key
            }
        }

        protocolMapping.forEach { protocol ->
            protocol.value.forEach {
                depProtocolMap[it] = protocol.key
            }
        }
    }

    companion object {
        fun byLanguage(language: String): FrameworkMarkup? {
            val markups = loadFrameworkMaps()
            return extendLanguage(markups)[language]
        }

        fun fromResource(): Array<FrameworkMarkup> {
            val markups = loadFrameworkMaps()
            return extendLanguage(markups).map { it.value }.toTypedArray()
        }

        private fun extendLanguage(markups: Array<FrameworkMarkup>): MutableMap<String, FrameworkMarkup> {
            val needExtends: MutableMap<Language, Language> = mutableMapOf()

            val markupMap: MutableMap<String, FrameworkMarkup> = mutableMapOf()
            markups.forEach {
                if (it.extends.isNotEmpty()) {
                    needExtends[it.language] = it.extends
                }

                markupMap[it.language] = it
            }

            if (needExtends.isNotEmpty()) {
                needExtends.forEach {
                    if (markupMap[it.key] != null && markupMap[it.value] != null) {
                        markupMap[it.key]!!.appTypeMapping += markupMap[it.value]!!.appTypeMapping
                        markupMap[it.key]!!.protocolMapping += markupMap[it.value]!!.protocolMapping
                    }
                }
            }

            return markupMap
        }

        private fun loadFrameworkMaps(): Array<FrameworkMarkup> {
            val fileContent = this::class.java.classLoader.getResource("framework-maps.json").readText()
            return Json.decodeFromString(fileContent)
        }
    }
}
