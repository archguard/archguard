package org.archguard.linter.rule.layer

import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.OutputType
import org.archguard.meta.Slot

class LayerRuleSlot: Slot {
    override var material: Materials
        get() = TODO("Not yet implemented")
        set(value) {}
    override var outClass: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun ticket(): Coin {
        TODO("Not yet implemented")
    }

    override fun prepare(items: List<Any>): List<Any> {
        TODO("Not yet implemented")
    }

    override fun process(items: List<Any>): OutputType {
        TODO("Not yet implemented")
    }
}