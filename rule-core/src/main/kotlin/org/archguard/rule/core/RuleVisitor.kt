package org.archguard.rule.core

import org.archguard.meta.Slot

/**
 * the base items of visitor
 */
abstract class RuleVisitor(val data: Iterable<Any>) : Slot {
    abstract fun visitor(ruleSets: Iterable<RuleSet>): List<Issue>
    // for disabled rules, we can set in backend.
    // abstract fun concurrentVisitor(enabledRules: Map<String, Rule>, ruleSets: Iterable<RuleSet>): List<Issue>

    override fun process(items: List<Any>): List<Issue> {
        return this.visitor(items as Iterable<RuleSet>)
    }
}
