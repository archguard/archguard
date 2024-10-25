package org.archguard.scanner.analyser.layered

import org.archguard.scanner.architecture.base.CONTROLLERS
import org.archguard.scanner.architecture.base.LayeredChecker
import org.archguard.scanner.architecture.base.REPO
import org.archguard.scanner.architecture.base.SERVICES

class MvcLayeredIdentify : LayeredChecker {
    private var hasController: Boolean = false
    private var hasService: Boolean = false
    private var hasRepo: Boolean = false

    override fun addToIdentify(str: String) {
        val split = str.split(".")
        if (!this.hasController) {
            this.hasController = CONTROLLERS.contains(split.last())
        }

        if (!this.hasService) {
            this.hasService = SERVICES.contains(split.last())
        }

        if (!this.hasRepo) {
            this.hasRepo = REPO.contains(split.last())
        }
    }

    override fun canMarked(): Boolean {
        return this.hasController && this.hasService && this.hasRepo
    }
}