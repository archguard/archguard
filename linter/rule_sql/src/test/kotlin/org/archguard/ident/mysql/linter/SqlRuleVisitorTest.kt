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

    @Test
    internal fun not_percent_in_like_start() {
        val sql = "SELECT customer_name FROM customers WHERE last_name LIKE '%Sm%';"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("FuzzyPercentNotAtStart", results[0].name)
    }

    @Test
    internal fun table_name_limit() {
        val sql = "CREATE TABLE this_is_a_very_long_table_name_32 (id INT(11) PRIMARY KEY)"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitTableNameLength", results[0].name)
    }

    @Test
    internal fun table_name_and_field_in_kebab() {
        val sql = "CREATE TABLE NotSnake ()"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("SnakeCasing", results[0].name)
    }

    @Test
    internal fun insert_should_with_field() {
        val sql = "INSERT INTO user VALUES ('alicfeng',23);"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("InsertWithoutField", results[0].name)
    }

    @Test
    internal fun limit_join_in_update() {
        val sql = "SELECT columns FROM table1 INNER JOIN table2 ON table1.column = table2.column " +
                " INNER JOIN table3 ON table1.column = table3.column " +
                " INNER JOIN table4 ON table1.column = table4.column " +
                " INNER JOIN table5 ON table1.column = table5.column " +
                " INNER JOIN table6 ON table1.column = table6.column "

        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitJoins", results[0].name)
    }

    @Test
    internal fun at_least_primary_key() {
        val sql = "CREATE TABLE tb_emp3 (id INT(11), name VARCHAR(25));"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("AtLeastOnePrimaryKey", results[0].name)
    }

    @Test
    internal fun limit_column_size() {
        val sql = "CREATE TABLE tb_emp3 (id INT PRIMARY KEY, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT );"
        val stmt = CCJSqlParserUtil.parseStatements(sql)
        val visitor = SqlRuleVisitor(stmt.statements)
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitColumnSize", results[0].name)
    }
}