package org.archguard.domain.version

class VersionComparisonParser(private val text: String) {
    private val comparisonSymbol = listOf("<", ">", "=")

    private val length = text.length
    private var pos = 0

    fun startWithComparison(): Boolean {
        return comparisonSymbol.contains(text[pos].toString())
    }

    private fun isSymbolInNEXT() = pos < length && comparisonSymbol.contains(text[pos].toString())

    fun symbol(): String {
        val start = pos
        while (isSymbolInNEXT()) {
            pos++
        }

        return text.substring(start, pos)
    }

    fun version(): String {
        return text.substring(pos)
    }
}