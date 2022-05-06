package org.archguard.rule.core

abstract class RuleVisitor(val data: Iterable<Any>) {
    abstract fun visitor(ruleSets: Iterable<RuleSet>): List<Issue>
}
