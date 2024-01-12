package com.thoughtworks.archguard.change.controller.response

import com.thoughtworks.archguard.change.domain.model.GitHotFilePO
import org.archguard.model.vos.JClassVO

class GitHotFileDTO(private val gitHotFilePO: GitHotFilePO) {
    val jclassId: String
        get() = gitHotFilePO.jclassId!!

    val systemId: Long
        get() = gitHotFilePO.systemId

    val moduleName: String
        get() {
            return if (gitHotFilePO.moduleName != null) return gitHotFilePO.moduleName else ""
        }

    val packageName: String
        get() = JClassVO(gitHotFilePO.className!!, moduleName).getPackageName()

    val typeName: String
        get() = JClassVO(gitHotFilePO.className!!, moduleName).getTypeName()

    val modifiedCount: Int
        get() = gitHotFilePO.modifiedCount
}
