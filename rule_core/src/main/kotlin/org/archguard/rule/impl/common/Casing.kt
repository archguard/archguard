package org.archguard.rule.impl.common

class Casing {
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
            val isNeedToFlag = it.isNotEmpty() && !this.isflat(it)
            if (isNeedToFlag) {
                if (this.IsPacal(it)) {
                    addCasingType(multipleNaming, "pascal", it)
                }
                if (this.`is-kebab`(it)) {
                    addCasingType(multipleNaming, "kebab", it)
                }
                if (this.`IS-COBOL`(it)) {
                    addCasingType(multipleNaming, "cobol", it)
                }
                if (this.is_nake(it)) {
                    addCasingType(multipleNaming, "snake", it)
                }
                if (this.IS_MACRO(it)) {
                    addCasingType(multipleNaming, "macro", it)
                }
                if (this.isCamel(it)) {
                    addCasingType(multipleNaming, "camel", it)
                }
            }
        }

        return multipleNaming
    }

    private fun addCasingType(
        multipleNaming: MutableMap<String, MutableList<String>>,
        key: String,
        it: String
    ) {
        val camel = multipleNaming.getOrDefault(key, mutableListOf())
        camel.add(it)
        multipleNaming[key] = camel
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