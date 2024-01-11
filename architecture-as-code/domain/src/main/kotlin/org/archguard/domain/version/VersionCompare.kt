package org.archguard.domain.version


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
}