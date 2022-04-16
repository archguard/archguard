package org.archguard.rule.impl

import org.archguard.rule.core.Rule

class CasingRule: Rule() {
    init {
        this.name = "Casing"
        this.key = this.javaClass.name
        this.description = "Some casing description"
    }
}
