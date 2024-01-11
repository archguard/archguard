package org.archguard.domain.version

val COMPARISON_SYMBOL = listOf("<", ">", "=")

class VersionComparisonParser(private val text: String) {
    private val length = text.length
    private var pos = 0

    fun startWithComparison(): Boolean {
        return COMPARISON_SYMBOL.contains(text[pos].toString())
    }

    private fun isSymbolInNEXT() = pos < length && COMPARISON_SYMBOL.contains(text[pos].toString())

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