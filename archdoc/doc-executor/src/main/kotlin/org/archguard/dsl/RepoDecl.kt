package org.archguard.dsl

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class RepoDecl(val name: String, val language: String, val scmUrl: String) : Element {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}

class ReposDecl : Element {
    var repos: List<RepoDecl> = listOf()

    fun repo(name: String, language: String, scmUrl: String) {
        this.repos += RepoDecl(name, language, scmUrl)
    }
}

fun repos(init: ReposDecl.() -> Unit): ReposDecl {
    val reposDecl = ReposDecl()
    reposDecl.init()

    archdoc.repos = reposDecl

    return reposDecl
}
