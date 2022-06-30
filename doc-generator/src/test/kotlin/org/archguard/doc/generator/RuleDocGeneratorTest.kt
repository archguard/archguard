package org.archguard.doc.generator

import org.archguard.linter.rule.sql.SqlRuleSetProvider
import org.archguard.linter.rule.sql.rules.LikeStartWithoutPercentRule
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RuleDocGeneratorTest {
    @Test
    internal fun should_count_sql_nodes() {
        val ruleDocGenerator = RuleDocGenerator()

        val sqlNodes = ruleDocGenerator.        // 1. load ruleset to maps

        // todo: 2. load test as error cases
        nodeFromRules(SqlRuleSetProvider().get().rules)
        assert(sqlNodes.content.size > 20)

        val node = ruleDocGenerator.nodeFromRules(arrayOf(LikeStartWithoutPercentRule()))
        assertEquals(4, node.content.size)
        assertEquals("org.archguard.doc.generator.render.DocHeader", node.content[0].javaClass.name)
    }

    @Test
    internal fun should_convert_text() {
        val ruleDocGenerator = RuleDocGenerator()

        val sqlNodes = ruleDocGenerator.        // 1. load ruleset to maps

        // todo: 2. load test as error cases
        nodeFromRules(SqlRuleSetProvider().get().rules)
        assert(sqlNodes.content.size > 20)

        val node = ruleDocGenerator.nodeFromRules(arrayOf(LikeStartWithoutPercentRule()))
        val text = ruleDocGenerator.stringify(node)
        assertEquals("""## like-start-without-percent

className: org.archguard.linter.rule.sql.rules.LikeStartWithoutPercentRule

description: 使用 like 模糊匹配时，查找字符串中通配符 % 放首位会导致无法使用索引。

severity: INFO

""", text)
    }
}