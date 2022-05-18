package org.archguard.dsl

import org.archguard.dsl.model.Repository

class ReposDecl : Element {
    var repos: List<Repository> = listOf()

    fun repo(name: String, language: String, scmUrl: String) {
        this.repos += Repository(name, language, scmUrl)
    }
}

fun repos(init: ReposDecl.() -> Unit): ReposDecl {
    val reposDecl = ReposDecl()
    reposDecl.init()

    archdoc.repos = reposDecl

    return reposDecl
}
