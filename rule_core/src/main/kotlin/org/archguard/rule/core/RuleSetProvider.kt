package org.archguard.rule.core

interface RuleSetProvider {
    public fun get(): RuleSet
}