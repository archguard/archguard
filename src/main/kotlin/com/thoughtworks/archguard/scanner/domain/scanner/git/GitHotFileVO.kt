package com.thoughtworks.archguard.scanner.domain.scanner.git

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
        if (path.contains("/src/")) {
            val pathBeforeSrc = path.substringBefore("/src")
            val indexOfLastSlash = pathBeforeSrc.indexOfLast { it == '/' }
            return pathBeforeSrc.substring(indexOfLastSlash + 1)
        }

        return null
    }
}
