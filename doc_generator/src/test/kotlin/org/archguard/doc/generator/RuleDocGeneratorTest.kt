package org.archguard.doc.generator

import org.archguard.linter.rule.sql.rules.LikeStartWithoutPercentRule
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RuleDocGeneratorTest {
    @Test
    internal fun should_count_sql_nodes() {
        val ruleDocGenerator = RuleDocGenerator()

        val sqlNodes = ruleDocGenerator.sqlNodes()
        assert(sqlNodes.content.size > 20)

        val node = ruleDocGenerator.nodeFromRules(arrayOf(LikeStartWithoutPercentRule()))
        assertEquals(3, node.content.size)
        assertEquals("org.archguard.doc.generator.render.DocHeader", node.content[0].javaClass.name)
    }
}