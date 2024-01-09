package org.archguard.architecture.layered

import org.archguard.architecture.core.CodeStructureStyle

class LayeredIdentify(private val packages: List<String>) {
    private var ddd = DddLayeredChecker()
    private var mvc = MvcLayeredIdentify()

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