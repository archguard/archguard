package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFile(val systemId: Long, val name: String, val modifiedCount: Int) {
    private val jvmClassSuffix: List<String> = listOf(".java", ".kt")
    
    fun isJVMClass(): Boolean {
        return jvmClassSuffix.filter { name.endsWith(it) }.any()
    }

}