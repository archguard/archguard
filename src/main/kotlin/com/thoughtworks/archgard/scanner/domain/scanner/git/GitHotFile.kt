package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFile(val systemId: Long, val name: String, val modifiedCount: Int) {
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

}