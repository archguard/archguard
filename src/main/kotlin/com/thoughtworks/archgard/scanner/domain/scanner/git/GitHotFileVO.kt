package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFileVO(val name: String, val modifiedCount: Int) {
    private val jvmClassSuffixMap: Map<String, String> = mapOf("java" to ".java", "kotlin" to ".kt")

    fun isJVMClass(): Boolean {
        return jvmClassSuffixMap.values.filter { name.endsWith(it) }.any()
    }

    fun className(): String? {
        return jvmClassSuffixMap.entries
                .filter { name.endsWith(it.value) }
                .map { name.substringAfter("${it.key}/").substringBefore(it.value).replace("/", ".") }
                .firstOrNull()
    }

    fun moduleName(): String? {
        if (name.startsWith("src/")) return null
        if (name.contains("/src/")) return name.substringBefore("/src")
        return null
    }

}