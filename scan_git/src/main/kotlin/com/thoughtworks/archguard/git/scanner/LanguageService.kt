// based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.
// SPDX-License-Identifier: MIT OR Unlicense

package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.model.Language
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class LanguageService {
    private val extensionCache: HashMap<String, String> = hashMapOf()
    private val SHE_BANG: String = "#!"
    private var extToLanguages: MutableMap<String, List<String>> = mutableMapOf()
    private var filenameToLanguage: MutableMap<String, String> = mutableMapOf()
    private var languageMap: MutableMap<String, Language> = mutableMapOf()

    init {
        val fileContent = this.javaClass.classLoader.getResource("languages.json").readText()
        val languages = Json.decodeFromString<Array<Language>>(fileContent)
        languages.forEach { entry ->
            languageMap[entry.name] = entry
            entry.extensions.forEach {
                if (extToLanguages[it] == null) {
                    extToLanguages[it] = listOf()
                }

                extToLanguages[it] = extToLanguages[it]!!.plus(entry.name)
            }
            entry.fileNames?.forEach {
                filenameToLanguage[it] = entry.name
            }
        }
    }

    fun determineLanguage(name: String): String {
        val langs = this.detectLanguages(name)
        if (langs.size == 1) {
            return langs[0]
        }

        var primaryLanguage = ""
        langs.forEach {
            if (languageMap[it]?.keywords.isNullOrEmpty()) {
                primaryLanguage = it
            }
        }

        return primaryLanguage
    }

    fun detectLanguages(name: String): List<String> {
        val language: MutableList<String> = mutableListOf()

        val dotCount = name.count { it == '.' }

        // such as `.gitignore` file
        val isDotFile = name[0] == '.' && dotCount == 1
        val notExtensionName = dotCount == 0
        val ifNeedToCheckFullName = notExtensionName || isDotFile
        if (ifNeedToCheckFullName) {
            val optFilenameLang = filenameToLanguage[name.lowercase()]
            if (optFilenameLang != null) {
                return listOf(optFilenameLang)
            }

            // make others file a shebang
            language += SHE_BANG
        }

        // Lookup in case the full name matches
        val fullNameExt = extToLanguages[name.lowercase()]
        if (fullNameExt != null) {
            return fullNameExt
        }

        val ext = this.getExtension(name)
        val optLang = extToLanguages[ext.lowercase()]
        if (optLang != null) {
            return optLang
        }

        // if multiple extension file is lost, fallback

        val secondExt = this.getExtension(ext)
        val fallbackLang = extToLanguages[secondExt.lowercase()]
        if (fallbackLang != null) {
            return fallbackLang
        }

        return language
    }

    fun getExtension(name: String): String {
        val lowercase = name.lowercase()
        var extension = extensionCache[lowercase]
        if (extension != null) {
            return extension
        }

        val ext = File(name).extension
        if (ext == "" || name.last() == '.') {
            extension = name
        } else {
            // Handling multiple dots or multiple extensions only needs to delete the last extension
            // and then call filepath.Ext.
            // If there are multiple extensions, it is the value of subExt,
            // otherwise subExt is an empty string.
            val subExt = File(name.removeSuffix(".$ext")).extension
            extension = "$subExt.$ext".removePrefix(".")
        }

        extensionCache[name] = extension
        return extension
    }
}