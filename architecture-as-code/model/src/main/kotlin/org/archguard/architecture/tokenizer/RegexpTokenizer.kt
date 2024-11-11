package org.archguard.architecture.tokenizer

interface RegexTokenizerOptions {
    val pattern: Regex?
    val discardEmpty: Boolean
    val gaps: Boolean?
}

open class RegexpTokenizer(opts: RegexTokenizerOptions? = null) : Tokenizer {
    var whitespacePattern = Regex("\\s+")
    var discardEmpty: Boolean = true
    private var _gaps: Boolean? = null

    init {
        val options = opts ?: object : RegexTokenizerOptions {
            override val pattern: Regex? = null
            override val discardEmpty: Boolean = true
            override val gaps: Boolean? = null
        }

        whitespacePattern = options.pattern ?: whitespacePattern
        discardEmpty = options.discardEmpty
        _gaps = options.gaps

        if (_gaps == null) {
            _gaps = true
        }
    }

    override fun tokenize(input: String): List<String> {
        val results: List<String>

        val output = if (_gaps == true) {
            results = input.split(whitespacePattern)
            if (discardEmpty) without(results, "", " ") else results
        } else {
            results = whitespacePattern.findAll(input).map { it.value }.toList()
            results.ifEmpty { emptyList() }
        }

        return output
    }

    fun without(arr: List<String>, vararg values: String): List<String> {
        return arr.filter { it !in values }
    }
}

class WordTokenizer(options: RegexTokenizerOptions? = null) : RegexpTokenizer(options) {
    init {
        whitespacePattern = Regex("[^A-Za-zА-Яа-я0-9_]+")
    }
}