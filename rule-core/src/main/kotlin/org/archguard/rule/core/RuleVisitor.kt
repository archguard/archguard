package org.archguard.rule.core

/**
 * the base items of visitor
 */
abstract class RuleVisitor(val data: Iterable<Any>) {
    abstract fun visitor(ruleSets: Iterable<RuleSet>): List<Issue>
}
