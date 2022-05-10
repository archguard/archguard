package org.archguard.dsl

class RepoDecl(val name: String, val language: String, val scmUrl: String) : Element

class ReposDecl : Element {
    var repos: List<RepoDecl> = listOf()

    fun repo(name: String, language: String, scmUrl: String) {
        this.repos += RepoDecl(name, language, scmUrl)
    }
}

fun repos(init: ReposDecl.() -> Unit): ReposDecl {
    val reposDecl = ReposDecl()
    reposDecl.init()
    return reposDecl
}
