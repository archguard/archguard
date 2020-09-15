package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFile(val systemId: Long, val name: String, val modifiedCount: Int) {
    private val jvmClassSuffix: List<String> = listOf(".java", ".kt")
    
    fun isJVMClass(): Boolean {
        return jvmClassSuffix.filter { name.endsWith(it) }.any()
    }

    fun className(): String {
        if (!isJVMClass()) return ""
        if (name.endsWith(".java")) return name.substringAfter("java/").replace("/", ".").substringBefore(".java")
        if (name.endsWith(".kt")) return name.substringAfter("kotlin/").replace("/", ".").substringBefore(".kt")
        return ""
    }

}