package org.archguard.doc.generator

import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.junit.jupiter.api.Test

internal class RuleGeneratorTest {
    @Test
    internal fun name() {
        val provider = SqlRuleSetProvider()
        provider.get().forEach {
            println(it.key)
            println(it.description)
        }
    }
}