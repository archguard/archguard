package com.thoughtworks.archguard.linter.checker


class CasingChecker {
    // may be unneed
    val FLAT_CASING = Regex("[a-z][a-z0-9]*")
    val CAMEL_CASING = Regex("[a-z][a-z0-9]*(?:[A-Z0-9](?:[a-z0-9]+|\$))*")
    val PASCAL_CASING = Regex("[A-Z][a-z0-9]*(?:[A-Z0-9](?:[a-z0-9]+|\$))*")
    val KEBAB_CASING = Regex("[a-z][a-z0-9]*(?:-[a-z0-9]+)*")
    val COBOL_CASING = Regex("[A-Z][A-Z0-9]*(?:-[A-Z0-9]+)")
    val SNAKE_CASING = Regex("[a-z][a-z0-9]*(?:_[a-z0-9]+)*")
    val MACRO_CASING = Regex("[A-Z][A-Z0-9]*(?:_[A-Z0-9]+)*")

    // PascalNaming
    fun isPacal(s: String): Boolean {
        return PASCAL_CASING.matches(s)
    }

    // kebab-naming
    fun isKebab(s: String): Boolean {
        return KEBAB_CASING.matches(s)
    }

    // snake_naming
    fun isSnake(s: String): Boolean {
        return SNAKE_CASING.matches(s)
    }

    // SNAKE-NAMING
    fun isCOBOL(s: String): Boolean {
        return COBOL_CASING.matches(s)
    }

    // MACRO_NAMING
    fun isMacro(s: String): Boolean {
        return MACRO_CASING.matches(s)
    }

    // camelNaming
    fun isCamel(s: String): Boolean {
        return CAMEL_CASING.matches(s)
    }
}