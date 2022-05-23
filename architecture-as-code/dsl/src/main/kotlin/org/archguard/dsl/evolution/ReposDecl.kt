package org.archguard.dsl.evolution

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.dsl.base.Element
import org.archguard.dsl.base.model.ActionType
import org.archguard.dsl.base.model.GraphType
import org.archguard.dsl.base.model.ReactiveAction
import org.archguard.dsl.base.model.Repository

class ReposDecl : Element {
    var repos: List<Repository> = listOf()

    fun repo(name: String, language: String, scmUrl: String) {
        this.repos += Repository(name, language, scmUrl)
    }

    fun create(): ReactiveAction {
        return ReactiveAction(
            ActionType.CREATE_REPOS,
            Repository.Companion::class.java.name,
            GraphType.NULL,
            Json.encodeToString(this.repos)
        )
    }
}
