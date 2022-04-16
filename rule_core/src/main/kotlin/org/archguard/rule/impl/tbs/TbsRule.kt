package org.archguard.rule.impl.tbs

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Rule

open class TbsRule: Rule() {
    override fun visit(rootNode: CodeDataStruct, callback: () -> Unit) {
        if(this.given.isNotEmpty()) {
            processGivenCondition()
        } else {
            // todo: call by interface
        }
    }

    private fun processGivenCondition() {

    }
}