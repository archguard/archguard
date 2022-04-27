package org.archguard.architecture.detect

import org.archguard.architecture.core.CodeStructureStyle

var CONTROLLERS: List<String> = listOf(
    "controller", "interface",
)

var DOMAIN: List<String> = listOf(
    "domain"
)

var REPO_IMPL: List<String> = listOf(
    "repositoryImpl", "repoImpl"
)

var REPO: List<String> = listOf(
    "repo"
)

var INFRA: List<String> = listOf(
    "infrastructure", "infra"
)

var APPLICATION: List<String> = listOf(
    "application", "app"
)

class DddLayeredIdentify() {
    private var hasDomain: Boolean = false
    private var hasInfra: Boolean = false
    private var hasInterfaces: Boolean = false
    private var hasApp: Boolean = false

    fun addToIdentify(it: String) {
        val split = it.split(".")
        if(!this.hasDomain) {
            this.hasDomain = DOMAIN.contains(split.last())
        }

        if(!this.hasInterfaces) {
            this.hasInterfaces = CONTROLLERS.contains(split.last())
        }

        if(!this.hasInfra) {
            this.hasInfra = INFRA.contains(split.last())
        }

        if(!this.hasApp) {
            this.hasApp = APPLICATION.contains(split.last())
        }
    }

    fun isMarked(): Boolean {
        return this.hasInterfaces && this.hasDomain && this.hasApp && this.hasInfra
    }

}

class LayeredIdentify(private val packages: List<String>) {
    var ddd = DddLayeredIdentify();
    fun identify(): CodeStructureStyle {
        packages.forEach {
            ddd.addToIdentify(it)
        }

        if (ddd.isMarked()) {
            return CodeStructureStyle.DDD
        }

        return CodeStructureStyle.UNKNOWN
    }
}