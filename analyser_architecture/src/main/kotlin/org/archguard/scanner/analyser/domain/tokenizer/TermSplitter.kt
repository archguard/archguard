package org.archguard.scanner.analyser.domain.tokenizer

/**
 * The `TermSplitter` object provides methods for splitting an input string into terms based on different naming styles of identifiers.
 * It supports three naming styles: CamelCase, Numeric, and underscore_case.
 *
 * The `splitTerms` method is a static generator function that splits the input string into terms based on the naming style of the identifiers.
 * It matches all patterns in the input string, creates a set to store unique terms, and adds lowercase versions of the matches to this set.
 * It then splits the matches into terms based on CamelCase, underscore_case, and Numeric naming styles, and adds these terms to the terms array.
 * If the term is longer than 2 characters and matches the alphabetic pattern, it is added to the set of unique terms.
 * Finally, it yields each unique term as a sequence.
 *
 * The `syncSplitTerms` method synchronously splits the input string into terms and returns a list of unique terms.
 */
object TermSplitter {
    private val allPattern = Regex("(?<![\\p{Alpha}\\d_$])[\\p{Alpha}_$][\\p{Alpha}\\d_$]{2,}(?![\\p{Alpha}\\d_$])")
    private val camelCasePattern = Regex("(?<=[a-z\$])(?=[A-Z])")
    private val numericPattern = Regex("^(\\D+)\\d+$")
    private val alphaNumericPattern = Regex("[\\p{Alpha}_\$]{3,}")

    /**
     * The `splitTerms` method is a static generator function that splits the input string into terms based on the naming style of the identifiers.
     * It supports three naming styles: CamelCase, Numeric, and underscore_case.
     *
     * @param input - The input string to be split into terms.
     *
     * The method works as follows:
     * 1. It matches all patterns in the input string.
     * 2. For each match, it creates a new set to store unique terms, and adds the lowercase version of the match to this set.
     * 3. It then splits the match into terms based on the CamelCase, underscore_case, and Numeric naming styles, and adds these terms to the terms array.
     * 4. If the term is longer than 2 characters and matches the alphabetic pattern, it is added to the set of unique terms.
     * 5. Finally, it yields each unique term.
     *
     * Note: The method uses the `findAll`, `split`, and `find` methods of the String object, and the `add`
     * method of the Set object. It also uses the spread operator (...) to add multiple elements to an array.
     *
     * @return A sequence of unique terms.
     */
    fun splitTerms(input: String): Sequence<String> {
        return sequence {
            val matchAll = allPattern.findAll(input)
            for (match in matchAll) {
                val matchValue = match.value

                val uniqueTerms = mutableSetOf<String>()
                uniqueTerms.add(matchValue.lowercase())

                val terms = mutableListOf<String>()

                val camelCaseSplits = matchValue.split(camelCasePattern)
                if (camelCaseSplits.size > 1) {
                    terms.addAll(camelCaseSplits)
                }

                val underscoreSplit = matchValue.split('_')
                if (underscoreSplit.size > 1) {
                    terms.addAll(underscoreSplit)
                }

                val numberSuffixMatch = numericPattern.find(matchValue)
                numberSuffixMatch?.let {
                    terms.add(it.groupValues[1])
                }

                terms.filter { term -> term.length > 2 && alphaNumericPattern.containsMatchIn(term) }
                    .forEach {
                        uniqueTerms.add(it.lowercase())
                    }

                yieldAll(uniqueTerms)
            }
        }.distinct()
    }

}
