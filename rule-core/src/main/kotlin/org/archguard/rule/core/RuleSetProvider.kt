package org.archguard.rule.core

import java.io.Serializable

interface RuleSetProvider: Serializable {
    fun get(): RuleSet
}