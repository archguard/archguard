package org.archguard.rule.core

open class RuleSet(
    var type: RuleType,
    val name: String,
    vararg val rules: Rule
) : Iterable<Rule> {
    override fun iterator(): Iterator<Rule> = rules.iterator()
}
