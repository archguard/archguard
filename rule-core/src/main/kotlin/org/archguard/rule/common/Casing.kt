package org.archguard.rule.common

val FLAT_CASING = Regex("[a-z][a-z0-9]*")
val CAMEL_CASING = Regex("[a-z][a-z0-9]*(?:[A-Z0-9](?:[a-z0-9]+|\$))*")
val PASCAL_CASING = Regex("[A-Z][a-z0-9]*(?:[A-Z0-9](?:[a-z0-9]+|\$))*")
val KEBAB_CASING = Regex("[a-z][a-z0-9]*(?:-[a-z0-9]+)*")
val COBOL_CASING = Regex("[A-Z][A-Z0-9]*(?:-[A-Z0-9]+)")
val SNAKE_CASING = Regex("[a-z][a-z0-9]*(?:_[a-z0-9]+)*")
val MACRO_CASING = Regex("[A-Z][A-Z0-9]*(?:_[A-Z0-9]+)*")

class Casing {
    companion object {
        // SNAKE-NAMING
        fun `IS-COBOL`(s: String): Boolean {
            return COBOL_CASING.matches(s)
        }

        // flat
        fun isflat(s: String): Boolean {
            return FLAT_CASING.matches(s)
        }

        // PascalNaming
        fun IsPacal(s: String): Boolean {
            return PASCAL_CASING.matches(s)
        }

        // kebab-naming
        fun `is-kebab`(s: String): Boolean {
            return KEBAB_CASING.matches(s)
        }

        // snake_naming
        fun is_nake(s: String): Boolean {
            return SNAKE_CASING.matches(s)
        }

        // MACRO_NAMING
        fun IS_MACRO(s: String): Boolean {
            return MACRO_CASING.matches(s)
        }

        // camelNaming
        fun isCamel(s: String): Boolean {
            return CAMEL_CASING.matches(s)
        }
    }
}