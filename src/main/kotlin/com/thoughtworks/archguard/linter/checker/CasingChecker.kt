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

    fun checkNaming(list: List<String>): MutableMap<String, MutableList<String>> {
        val multipleNaming: MutableMap<String, MutableList<String>> = hashMapOf()
        list.forEach {
            if (it.isNotEmpty()) {
                if (this.IsPacal(it)) {
                    val key = "pascal"
                    val pascal = multipleNaming.getOrDefault(key, mutableListOf())
                    pascal.add(it)
                    multipleNaming[key] = pascal
                }
                if (this.`is-kebab`(it)) {
                    val key = "kebab"
                    val kebab = multipleNaming.getOrDefault(key, mutableListOf())
                    kebab.add(it)
                    multipleNaming[key] = kebab
                }
                if (this.`IS-COBOL`(it)) {
                    val key = "cobol"
                    val cobol = multipleNaming.getOrDefault(key, mutableListOf())
                    cobol.add(it)
                    multipleNaming[key] = cobol
                }
                if (this.is_nake(it)) {
                    val key = "snake"
                    val snake = multipleNaming.getOrDefault(key, mutableListOf())
                    snake.add(it)
                    multipleNaming[key] = snake
                }
                if (this.IS_MACRO(it)) {
                    val key = "macro"
                    val macro = multipleNaming.getOrDefault(key, mutableListOf())
                    macro.add(it)
                    multipleNaming[key] = macro
                }
                if (this.isCamel(it)) {
                    val key = "camel"
                    val camel = multipleNaming.getOrDefault(key, mutableListOf())
                    camel.add(it)
                    multipleNaming[key] = camel
                }
            }
        }

        return multipleNaming
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

    // SNAKE-NAMING
    fun `IS-COBOL`(s: String): Boolean {
        return COBOL_CASING.matches(s)
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