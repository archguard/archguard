package org.archguard.linter.rule.sql

import org.junit.jupiter.api.Test


// test should support close to RuleName
internal class SqlRuleVisitorTest {
    @Test
    internal fun test_rule_unknown_column_size() {
        val visitor = SqlRuleVisitor(listOf("SELECT * FROM tab2"))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("UnknownColumnSize", results[0].name)
    }

    @Test
    internal fun test_rule_like_start_without_percent() {
        val sql = "SELECT customer_name FROM customers WHERE last_name LIKE '%Sm%';"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LikeStartWithoutPercent", results[0].name)
    }

    @Test
    internal fun test_rule_limit_table_name_length() {
        val sql = "CREATE TABLE this_is_a_very_long_table_name_32 (id INT(11) PRIMARY KEY)"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitTableNameLength", results[0].name)
    }

    @Test
    internal fun test_rule_snake_case_naming() {
        val sql = "CREATE TABLE NotSnake ()"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("SnakeCaseNaming", results[0].name)
    }

    @Test
    internal fun test_rule_insert_without_field() {
        val sql = "INSERT INTO user VALUES ('alicfeng',23);"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("InsertWithoutField", results[0].name)
    }

    @Test
    internal fun test_rule_limit_joins() {
        val sql = "SELECT columns FROM table1 INNER JOIN table2 ON table1.column = table2.column " +
                " INNER JOIN table3 ON table1.column = table3.column " +
                " INNER JOIN table4 ON table1.column = table4.column " +
                " INNER JOIN table5 ON table1.column = table5.column " +
                " INNER JOIN table6 ON table1.column = table6.column "

        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitJoins", results[0].name)
    }

    @Test
    internal fun test_rule_at_least_one_primary_key() {
        val sql = "CREATE TABLE tb_emp3 (id INT(11), name VARCHAR(25));"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("AtLeastOnePrimaryKey", results[0].name)
    }

    @Test
    internal fun test_rule_limit_column_size() {
        val sql = "CREATE TABLE tb_emp3 (id INT PRIMARY KEY, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT, i1 INT, i1 INT, i1 INT, i1 INT, " +
                "i1 INT );"
        val visitor = SqlRuleVisitor(listOf(sql))
        val ruleSetProvider = SqlRuleSetProvider()

        val results = visitor.visitor(listOf(ruleSetProvider.get()))
        kotlin.test.assertEquals(1, results.size)
        kotlin.test.assertEquals("LimitColumnSize", results[0].name)
    }
}