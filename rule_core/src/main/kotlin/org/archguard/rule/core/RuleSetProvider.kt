package org.archguard.rule.core

interface RuleSetProvider {
    fun get(): RuleSet
}