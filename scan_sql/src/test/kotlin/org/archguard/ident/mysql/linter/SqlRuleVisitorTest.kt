package org.archguard.ident.mysql.linter

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import org.junit.jupiter.api.Test

internal class SqlRuleVisitorTest {
    @Test
    internal fun not_uppercase() {
        val stmt = CCJSqlParserUtil.parseStatements("SELECT * FROM tab2")
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("UnknownNumberColumn", results[0].name)
    }
}