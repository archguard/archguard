package org.archguard.rule.core

/**
 * the base items of visitor
 */
abstract class RuleVisitor(val data: Iterable<Any>) {
    abstract fun visitor(ruleSets: Iterable<RuleSet>): List<Issue>
    // for disabled rules, we can set in backend.
    // abstract fun concurrentVisitor(enabledRules: Map<String, Rule>, ruleSets: Iterable<RuleSet>): List<Issue>

//    // accept rules to continue process
//    override fun prepare(items: List<Any>): List<RuleSet> {
//        return listOf()
//    }
//
//    override fun process(items: List<Any>): List<Issue> {
//        return this.visitor(items as Iterable<RuleSet>)
//    }
}
