package org.archguard.rule.core


abstract class RuleVisitor(val data: Iterable<Any>) {
    /* for analysis with source code */
    abstract fun requiredDataType(): List<String>

    abstract fun visitor(ruleSets: Iterable<RuleSet>): List<Issue>
    // for disabled rules, we can set in backend.
    // abstract fun concurrentVisitor(enabledRules: Map<String, Rule>, ruleSets: Iterable<RuleSet>): List<Issue>
}
