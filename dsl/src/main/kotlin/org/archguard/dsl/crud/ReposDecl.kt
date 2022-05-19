package org.archguard.dsl.crud

import org.archguard.dsl.base.Element
import org.archguard.dsl.model.Repository

class ReposDecl : Element {
    var repos: List<Repository> = listOf()

    fun repo(name: String, language: String, scmUrl: String) {
        this.repos += Repository(name, language, scmUrl)
    }
}
