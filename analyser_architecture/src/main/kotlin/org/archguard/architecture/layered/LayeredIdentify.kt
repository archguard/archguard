package org.archguard.architecture.layered

import org.archguard.architecture.core.CodeStructureStyle

/**
 * The `LayeredIdentify` class is responsible for identifying the code structure style of a given list of packages.
 * It checks if the packages follow the Domain-Driven Design (DDD) or Model-View-Controller (MVC) architectural patterns.
 * If none of the patterns are detected, it returns `CodeStructureStyle.UNKNOWN`.
 *
 * @property packages The list of packages to be analyzed.
 * @property ddd The `DddLayeredChecker` instance used to check for DDD pattern.
 * @property mvc The `MvcLayeredIdentify` instance used to check for MVC pattern.
 */
class LayeredIdentify(private val packages: List<String>) {

    private var ddd = DddLayeredChecker()
    private var mvc = MvcLayeredIdentify()

    /**
     * Identifies the code structure style of the given packages.
     *
     * @return The detected code structure style (`CodeStructureStyle.DDD`, `CodeStructureStyle.MVC`, or `CodeStructureStyle.UNKNOWN`).
     */
    fun identify(): CodeStructureStyle {
        packages.forEach {
            ddd.addToIdentify(it)
            mvc.addToIdentify(it)
        }

        if (ddd.canMarked()) {
            return CodeStructureStyle.DDD
        }

        if (mvc.canMarked()) {
            return CodeStructureStyle.MVC
        }

        return CodeStructureStyle.UNKNOWN
    }
}