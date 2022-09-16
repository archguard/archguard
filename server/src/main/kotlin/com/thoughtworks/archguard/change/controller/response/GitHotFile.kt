package com.thoughtworks.archguard.change.controller.response

import com.thoughtworks.archguard.change.domain.model.GitHotFile
import com.thoughtworks.archguard.code.module.domain.model.JClassVO

class GitHotFile(private val gitHotFile: GitHotFile) {
    val jclassId: String
        get() = gitHotFile.jclassId!!

    val systemId: Long
        get() = gitHotFile.systemId

    val moduleName: String
        get() {
            return if (gitHotFile.moduleName != null) return gitHotFile.moduleName else ""
        }

    val packageName: String
        get() = JClassVO(gitHotFile.className!!, moduleName).getPackageName()

    val typeName: String
        get() = JClassVO(gitHotFile.className!!, moduleName).getTypeName()

    val modifiedCount: Int
        get() = gitHotFile.modifiedCount
}
