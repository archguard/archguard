package org.archguard.domain.version


data class VersionNumber(val major: Int, val minor: Int, val micro: Int, val patch: Int, val qualifier: String?) {
    operator fun compareTo(other: VersionNumber): Int {
        if (major != other.major) {
            return major - other.major
        }

        if (minor != other.minor) {
            return minor - other.minor
        }

        if (micro != other.micro) {
            return micro - other.micro
        }

        if (patch != other.patch) {
            return patch - other.patch
        }

        return qualifier?.lowercase().orEmpty().compareTo(other.qualifier?.lowercase().orEmpty())
    }

    companion object {
        fun parse(version: String): VersionNumber? {
            if (version.isEmpty()) return null

            val parser = VersionParser(version)
            if (!parser.startWithDigit()) {
                return null
            }

            var major = 0
            var minor = 0
            var micro = 0
            var patch = 0

            major = parser.digit()
            if (parser.isDotInNext()) {
                parser.nextChar()
                minor = parser.digit()
                if (parser.isDotInNext()) {
                    parser.nextChar()
                    micro = parser.digit()

                    if (parser.isSeparatorInNext()) {
                        parser.nextChar()
                        patch = parser.digit()
                    }
                }

            }

            return VersionNumber(major, minor, micro, patch, parser.qualifier())
        }
    }
}