package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFileVO(val path: String, val modifiedCount: Int) {
    private val jvmClassSuffixMap: Map<String, String> = mapOf("java" to ".java", "kotlin" to ".kt")

    fun isJVMClass(): Boolean {
        return jvmClassSuffixMap.values.filter { path.endsWith(it) }.any()
    }

    fun className(): String? {
        return jvmClassSuffixMap.entries
                .filter { path.endsWith(it.value) }
                .map { path.substringAfter("${it.key}/").substringBefore(it.value).replace("/", ".") }
                .firstOrNull()
    }

    fun moduleName(): String? {
        if (path.startsWith("src/")) return null
        if (path.contains("/src/")) return path.substringBefore("/src")
        return null
    }

}