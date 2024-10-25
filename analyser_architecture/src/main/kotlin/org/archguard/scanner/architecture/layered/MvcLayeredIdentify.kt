package org.archguard.scanner.architecture.layered

class MvcLayeredIdentify : org.archguard.scanner.architecture.layered.LayeredChecker {
    private var hasController: Boolean = false
    private var hasService: Boolean = false
    private var hasRepo: Boolean = false

    override fun addToIdentify(str: String) {
        val split = str.split(".")
        if (!this.hasController) {
            this.hasController = org.archguard.scanner.architecture.layered.CONTROLLERS.contains(split.last())
        }

        if (!this.hasService) {
            this.hasService = org.archguard.scanner.architecture.layered.SERVICES.contains(split.last())
        }

        if (!this.hasRepo) {
            this.hasRepo = org.archguard.scanner.architecture.layered.REPO.contains(split.last())
        }
    }

    override fun canMarked(): Boolean {
        return this.hasController && this.hasService && this.hasRepo
    }
}