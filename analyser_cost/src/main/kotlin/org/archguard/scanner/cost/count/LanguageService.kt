// based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.
// SPDX-License-Identifier: MIT OR Unlicense
// `languages.json` based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.

package org.archguard.scanner.cost.count

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.experimental.or

class LanguageService {
    private val extensionCache: HashMap<String, String> = hashMapOf()
    private val SHE_BANG: String = "#!"
    private var extToLanguages: MutableMap<String, List<String>> = mutableMapOf()
    private var filenameToLanguage: MutableMap<String, String> = mutableMapOf()
    private var languageMap: MutableMap<String, Language> = mutableMapOf()
    private var languageFeatures: MutableMap<String, LanguageFeature> = mutableMapOf()

    init {
        val fileContent = this.javaClass.classLoader.getResource("languages.json")!!.readText()
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
            processLanguageFeatures(entry.name, entry)
        }
    }

    fun determineLanguage(filename: String): String {
        val langs = this.detectLanguages(filename)
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

    fun detectLanguages(filename: String): List<String> {
        val language: MutableList<String> = mutableListOf()

        val dotCount = filename.count { it == '.' }

        // such as `.gitignore` file
        val isDotFile = filename[0] == '.' && dotCount == 1
        val notExtensionName = dotCount == 0
        val ifNeedToCheckFullName = notExtensionName || isDotFile
        if (ifNeedToCheckFullName) {
            val optFilenameLang = filenameToLanguage[filename.lowercase()]
            if (optFilenameLang != null) {
                return listOf(optFilenameLang)
            }

            // make others file a shebang
            language += SHE_BANG
        }

        // Lookup in case the full name matches
        val fullNameExt = extToLanguages[filename.lowercase()]
        if (fullNameExt != null) {
            return fullNameExt
        }

        val ext = this.getExtension(filename)
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

    private var Complexity = false
    private fun processLanguageFeatures(name: String, entry: Language) {
        val complexityTrie = Trie()
        val slCommentTrie = Trie()
        val mlCommentTrie = Trie()
        val stringTrie = Trie()
        val tokenTrie = Trie()

        var complexityMask: Byte = 0
        var singleLineCommentMask: Byte = 0
        var multiLineCommentMask: Byte = 0
        var stringMask: Byte = 0
        var processMask: Byte = 0

        entry.complexityChecks?.forEach {
            complexityMask = complexityMask or it[0]
            complexityTrie.insert(TokenType.TComplexity, it)
            if (!Complexity) {
                tokenTrie.insert(TokenType.TComplexity, it)
            }
        }
        if (!Complexity) {
            processMask = processMask or complexityMask
        }

        entry.lineComment?.forEach {
            singleLineCommentMask = singleLineCommentMask or it[0]
            slCommentTrie.insert(TokenType.TSlcomment, it)
            tokenTrie.insert(TokenType.TSlcomment, it)
        }
        processMask = processMask or singleLineCommentMask

        entry.multiLine?.forEach {
            multiLineCommentMask = multiLineCommentMask or it[0][0]
            mlCommentTrie.insertClose(TokenType.TMlcomment, it[0], it[1])
            tokenTrie.insertClose(TokenType.TMlcomment, it[0], it[1])
        }
        processMask = processMask or multiLineCommentMask

        entry.quotes?.forEach {
            stringMask = stringMask or it.start[0]
            stringTrie.insertClose(TokenType.TString, it.start, it.end)
            tokenTrie.insertClose(TokenType.TString, it.start, it.end)
        }
        processMask = processMask or stringMask

        languageFeatures[name] = LanguageFeature(
            complexityTrie,
            mlCommentTrie,
            slCommentTrie,
            stringTrie,
            tokenTrie,
            entry.nestedMultiLine,
            complexityMask,
            multiLineCommentMask,
            singleLineCommentMask,
            stringMask,
            processMask,
            entry.keywords,
            entry.quotes
        )
    }

    fun getLanguageFeature(language: String): LanguageFeature? {
        return languageFeatures[language]
    }
}

private infix fun Byte.or(c: Char): Byte {
    return this or c.toByte()
}

