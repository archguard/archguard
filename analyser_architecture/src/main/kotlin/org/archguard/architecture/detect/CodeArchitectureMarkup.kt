package org.archguard.architecture.detect

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
class CodeArchitectureMarkup(
    val language: String,
    @SerialName("app_types")
    val appTypes: HashMap<String, List<String>> = hashMapOf(),
    @SerialName("protocol_mapping")
    var protocolMapping: HashMap<String, List<String>> = hashMapOf(),
    val extends: String = "",
) {
    companion object {
        fun fromResource(): Array<CodeArchitectureMarkup> {
            val fileContent = this.javaClass.classLoader.getResource("frameworks.json").readText()
            return Json.decodeFromString(fileContent)
        }
    }
}
