package org.archguard.architecture.tokenizer

class CodeNamingTokenizer(opts: RegexTokenizerOptions? = null) : RegexpTokenizer(opts) {
    init {
        whitespacePattern = Regex(
            "(?<=[a-z])(?=[A-Z])|" +       // camelCase 分词
                    "(?<=[A-Z])(?=[A-Z][a-z])|" +  // PascalCase 分词
                    "(?<=[A-Za-z])(?=[0-9])|" +    // 字母和数字分词
                    "(?<=[0-9])(?=[A-Za-z])|" +    // 数字和字母分词
                    "_+"                           // under_score 分词
        )
    }

    /// number regex
    private val numberPattern = Regex("[0-9]+")

    override fun tokenize(input: String): List<String> {
        var results = whitespacePattern.split(input)
        /// without number
        results = results.filter {
            it.isNotEmpty() && !numberPattern.matches(it)
        }

        return if (discardEmpty) {
            without(results, "", " ").map { it.lowercase() }
        } else {
            results.map { it.lowercase() }
        }
    }
}