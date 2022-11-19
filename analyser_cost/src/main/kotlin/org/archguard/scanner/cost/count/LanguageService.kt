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
        val languageMap: HashMap<String, Language> = Json.decodeFromString(fileContent)

        languageMap.forEach { (name, lang) ->
            lang.name = name
            lang.extensions.forEach {
                if (extToLanguages[it] == null) {
                    extToLanguages[it] = listOf()
                }

                extToLanguages[it] = extToLanguages[it]?.plus(lang.name!!)!!
            }
            lang.fileNames?.forEach {
                filenameToLanguage[it] = lang.name!!
            }
            processLanguageFeatures(lang.name!!, lang)
        }
    }

    data class LanguageGuess(
        val name: String,
        val count: Int,
    )

    // DetermineLanguage given a filename, fallback language, possible languages and content make a guess to the type.
    // If multiple possible it will guess based on keywords similar to how https://github.com/vmchale/polyglot does
    fun determineLanguage(fallbackLanguage: String, possibleLanguages: List<String>, content: ByteArray): String {
        // If being called through an API its possible nothing is set here and as
        // such should just return as the Language value should have already been set
        if (possibleLanguages.isEmpty()) {
            return fallbackLanguage
        }

        // There should only be two possibilities now, either we have a single fallbackLanguage
        // in which case we set it and return
        // or we have multiple in which case we try to determine it heuristically
        if (possibleLanguages.size == 1) {
            return possibleLanguages[0]
        }

        val toCheck: String = if (content.size > 20000) {
            String(content.sliceArray(0..20000))
        } else {
            String(content)
        }

        var primary = ""

        val toSort = mutableListOf<LanguageGuess>()
        possibleLanguages.forEach { lang ->
            val langFeatures = languageFeatures[lang]!!

            var count = 0
            langFeatures.keywords?.forEach { key ->
                if (toCheck.contains(key)) {
                    count++
                }
            }

            if (langFeatures.keywords?.isEmpty() == true) {
                primary = lang
            }

            toSort.add(LanguageGuess(lang, count))
        }

        toSort.sortWith(compareByDescending<LanguageGuess> { it.count }.thenBy { it.name })

        if (primary.isNotEmpty() && toSort.isNotEmpty()) {
            if (toSort[0].count < 3) {
                return primary
            }
        }

        if (toSort.isNotEmpty()) {
            return toSort[0].name
        }

        return fallbackLanguage
    }


    /**
     * ByExtension given a filename return the language it is associated with
     */
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

    private var Complexity = true
    private fun processLanguageFeatures(name: String, value: Language) {
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

        value.complexityChecks?.forEach {
            complexityMask = complexityMask or it[0]
            complexityTrie.insert(TokenType.TComplexity, it)
            if (!Complexity) {
                tokenTrie.insert(TokenType.TComplexity, it)
            }
        }
        if (!Complexity) {
            processMask = processMask or complexityMask
        }

        value.lineComment?.forEach {
            singleLineCommentMask = singleLineCommentMask or it[0]
            slCommentTrie.insert(TokenType.TSlcomment, it)
            tokenTrie.insert(TokenType.TSlcomment, it)
        }
        processMask = processMask or singleLineCommentMask

        value.multiLine?.forEach {
            multiLineCommentMask = multiLineCommentMask or it[0][0]
            mlCommentTrie.insertClose(TokenType.TMlcomment, it[0], it[1])
            tokenTrie.insertClose(TokenType.TMlcomment, it[0], it[1])
        }
        processMask = processMask or multiLineCommentMask

        value.quotes?.forEach {
            stringMask = stringMask or it.start[0]
            stringTrie.insertClose(TokenType.TString, it.start, it.end)
            tokenTrie.insertClose(TokenType.TString, it.start, it.end)
        }
        processMask = processMask or stringMask

        languageFeatures[name] = LanguageFeature(
            complexity = complexityTrie,
            mlCommentTrie,
            slCommentTrie,
            stringTrie,
            tokens = tokenTrie,
            value.nestedMultiLine,
            complexityMask,
            multiLineCommentMask,
            singleLineCommentMask,
            stringMask,
            processMask,
            value.keywords,
            value.quotes
        )
    }

    fun getLanguageFeature(language: String): LanguageFeature? {
        return languageFeatures[language]
    }
}

private infix fun Byte.or(c: Char): Byte {
    return this or c.toByte()
}

