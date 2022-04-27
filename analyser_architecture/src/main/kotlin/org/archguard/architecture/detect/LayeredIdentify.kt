package org.archguard.architecture.detect

interface LayeredIdentify {

}

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

class MvcIdentify: LayeredIdentify {
    fun identify(packages: List<String>): Boolean {

        return false
    }
}