package org.archguard.domain.version


val comparisonSymbol = listOf("<", ">", "=")

class VersionComparisonParser(private val text: String) {
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


data class VersionCompare(val comparison: String, val version: String) {
    companion object {
        fun parse(string: String): VersionCompare? {
            if (string.isEmpty()) return null

            val text = string.replace("\\s".toRegex(), "")
            val scanner = VersionComparisonParser(text)

            if (!scanner.startWithComparison()) return null

            val comparison = scanner.symbol()
            val version = scanner.version()

            return VersionCompare(comparison, version)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VersionCompare) return false

        if (comparison != other.comparison) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = comparison.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }
}