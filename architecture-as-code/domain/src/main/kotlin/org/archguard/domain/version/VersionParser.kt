package org.archguard.domain.version

class VersionParser(private val versionStr: String) {
    private val dot = '.'
    private val length = versionStr.length
    private var pos = 0

    private fun isDigitInNext() = pos < length && versionStr[pos].isDigit()

    fun startWithDigit(): Boolean {
        return versionStr[0].isDigit()
    }

    fun digit(): Int {
        val start = pos
        while (isDigitInNext()) {
            pos++
        }

        val substring = versionStr.substring(start, pos)
        try {
            return substring.toInt()
        } catch (e: Exception) {
            // like "6.0.0.202111291000-r", move post to before last .
            pos = start
            pos--
            return 0
        }
    }

    fun isDotInNext(): Boolean {
        if (pos >= length - 1) {
            return false
        }

        if (versionStr[pos] == dot) {
            return versionStr[pos + 1].isDigit()

        }
        return false
    }

    fun nextChar() {
        pos++
    }

    fun isSeparatorInNext(): Boolean {
        if (pos >= length - 1) {
            return false
        }

        if (versionStr[pos] == dot || versionStr[pos] == '_') {
            return versionStr[pos + 1].isDigit()
        }

        return false
    }

    fun qualifier(): String? {
        if (pos == length) return null

        if (versionStr[pos] == dot || versionStr[pos] == '-') {
            nextChar()
            return versionStr.substring(pos)

        }

        return null
    }

}