package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFile(private val gitHotFileVO: GitHotFileVO, val systemId: Long, val repo: String, jclassId: String?) {
    val name: String
        get() = gitHotFileVO.name
    
    val modifiedCount: Int
        get() = gitHotFileVO.modifiedCount

    val moduleName: String?
        get() = gitHotFileVO.moduleName()
    
    val className: String?
        get() = gitHotFileVO.className()
}