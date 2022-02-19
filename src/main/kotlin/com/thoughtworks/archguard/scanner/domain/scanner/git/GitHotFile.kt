package com.thoughtworks.archguard.scanner.domain.scanner.git

data class GitHotFile(val systemId: Long, val repo: String, val path: String, val moduleName: String?, val className: String?, val modifiedCount: Int, val jclassId: String?)