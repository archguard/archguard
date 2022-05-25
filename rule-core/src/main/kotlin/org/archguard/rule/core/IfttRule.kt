package org.archguard.rule.core

abstract class IfttRule : Rule() {
    fun given(conditions: List<String>): IfttRule {
        return this
    }

    fun then(conditions: List<String>): IfttRule {
        return this
    }
}