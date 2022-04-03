package com.thoughtworks.archguard.change.domain

class GitHotFile(val systemId: Long, val repo: String, val path: String, val moduleName: String?, val className: String?, val modifiedCount: Int, val jclassId: String?)

class GitPathChangeCount(val systemId: Long, var path: String, val changes: Int, val lineCount: Int)