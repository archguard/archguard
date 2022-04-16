package org.archguard.rule.core

open class RuleSet(vararg val rules: Rule) : Iterable<Rule> {
    override fun iterator(): Iterator<Rule> = rules.iterator()
}
