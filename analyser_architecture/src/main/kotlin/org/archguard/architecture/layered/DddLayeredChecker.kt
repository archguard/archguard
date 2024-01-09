package org.archguard.architecture.layered

// todo: change to real-case
class DddLayeredChecker() : LayeredChecker {
    private var hasDomain: Boolean = false
    private var hasInfra: Boolean = false
    private var hasInterfaces: Boolean = false
    private var hasApp: Boolean = false

    override fun addToIdentify(str: String) {
        val split = str.split(".")
        if (!this.hasDomain) {
            this.hasDomain = DOMAIN.contains(split.last())
        }

        if (!this.hasInterfaces) {
            this.hasInterfaces = CONTROLLERS.contains(split.last())
        }

        if (!this.hasInfra) {
            this.hasInfra = INFRA.contains(split.last())
        }

        if (!this.hasApp) {
            this.hasApp = APPLICATION.contains(split.last())
        }
    }

    override fun canMarked(): Boolean {
        return this.hasInterfaces && this.hasDomain && this.hasApp && this.hasInfra
    }
}