package org.archguard.architecture.detect

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

typealias Language = String

@Serializable
class CodeArchitectureMarkup(
    val language: String,
    @SerialName("app_type_mapping")
    val appTypeMapping: HashMap<String, List<String>> = hashMapOf(),
    @SerialName("protocol_mapping")
    var protocolMapping: HashMap<String, List<String>> = hashMapOf(),
    val extends: String = "",
) {


    companion object {
        fun fromResource(): Array<CodeArchitectureMarkup> {
            val markups = loadFrameworkMaps()
            return extendLanguage(markups)
        }

        private fun extendLanguage(markups: Array<CodeArchitectureMarkup>): Array<CodeArchitectureMarkup> {
            val needExtends: MutableMap<Language, Language> = mutableMapOf()

            val markupMap: MutableMap<String, CodeArchitectureMarkup> = mutableMapOf()
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

            return markupMap.map { it.value }.toTypedArray()
        }

        private fun loadFrameworkMaps(): Array<CodeArchitectureMarkup> {
            val fileContent = this.javaClass.classLoader.getResource("framework-maps.json").readText()
            return Json.decodeFromString(fileContent)
        }
    }
}
