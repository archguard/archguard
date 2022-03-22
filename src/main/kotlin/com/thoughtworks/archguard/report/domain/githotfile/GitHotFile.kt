package com.thoughtworks.archguard.report.domain.githotfile

class GitHotFile(val systemId: Long, val repo: String, val path: String, val moduleName: String?, val className: String?, val modifiedCount: Int, val jclassId: String?) {
}

class GitPathChangeCount(val systemId: Long, val path: String, val changes: Int) {
}