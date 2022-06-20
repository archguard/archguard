package com.thoughtworks.archguard.insights.domain

class VersionParser(private val versionStr: String) {
    var pos = 0

    fun startWithDigit(): Boolean {
        return versionStr[0].isDigit()
    }

    fun digit(): Int {
        val start = pos
        while (isDigitInNext()) {
            pos++
        }

        return versionStr.substring(start, pos).toInt()
    }

    private fun isDigitInNext() = pos < length && versionStr[pos].isDigit()

    private val length = versionStr.length

    fun isDotInNext(): Boolean {
        if(pos >= length - 1) {
            return false
        }

        if (versionStr[pos] != '.') {
            return false
        }

        if (versionStr[pos + 1].isDigit()) {
            return true
        }

        return false
    }

    fun next() {
        pos++
    }

}

data class VersionNumber(val major: Int, val minor: Int, val micro: Int, val patch: Int, val qualifier: String?) {
    companion object {
        fun parse(version: String): VersionNumber? {
            if(version.isEmpty()) return null

            var major = 0
            var minor = 0
            var micro = 0
            val patch = 0

            val parser = VersionParser(version)
            if(!parser.startWithDigit()) {
                return null
            }

            major = parser.digit()
            if(parser.isDotInNext()) {
                parser.next()
                minor = parser.digit()
                if(parser.isDotInNext()) {
                    parser.next()
                    micro = parser.digit()
                }

            }

            return VersionNumber(major, minor, micro, patch, null)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VersionNumber) return false

        if (major != other.major) return false
        if (minor != other.minor) return false
        if (micro != other.micro) return false
        if (patch != other.patch) return false
        if (qualifier != other.qualifier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + micro
        result = 31 * result + patch
        result = 31 * result + (qualifier?.hashCode() ?: 0)
        return result
    }
}