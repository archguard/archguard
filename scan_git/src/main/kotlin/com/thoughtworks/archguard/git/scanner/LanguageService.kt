package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.model.Language
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class LanguageService {
    private var extToLanguage: MutableMap<String, String> = mutableMapOf()
    private var filenameToLanguage: MutableMap<String, String> = mutableMapOf()

    init {
        val fileContent = this.javaClass.classLoader.getResource("languages.json").readText()
        val languages = Json.decodeFromString<Array<Language>>(fileContent)
        languages.forEach { entry ->
            entry.extensions.forEach {
                extToLanguage[it] = entry.name
            }
            entry.fileNames?.forEach {
                filenameToLanguage[it] = entry.name
            }
        }
    }

    fun detectLanguage(name: String): String {
        val language = ""
        val dotCount = name.count { it == '.' }

        // such as `.gitignore` file
        val isDotFile = name[0] == '.' && dotCount == 1
        val notExtensionName = dotCount == 0
        val ifNeedToCheckFullName = notExtensionName || isDotFile
        if (ifNeedToCheckFullName) {
            val optFilenameLang = filenameToLanguage[name.lowercase()]
            if (optFilenameLang != null) {
                return optFilenameLang.toString()
            }
        }

        val dotSplit = name.split(".")
        val firstExt = dotSplit.last()
        val optLang = extToLanguage[firstExt.lowercase()]
        if (optLang != null) {
            return optLang.toString()
        }
        return language
    }
}