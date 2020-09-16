package com.thoughtworks.archgard.scanner.domain.scanner.git

class GitHotFile(val systemId: Long, val repo: String, val name: String, val moduleName: String?, val className: String?, val modifiedCount: Int, val jclassId: String?) {
}