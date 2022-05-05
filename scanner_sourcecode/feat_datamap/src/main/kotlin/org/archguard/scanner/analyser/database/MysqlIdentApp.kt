package org.archguard.scanner.analyser.database

import mu.KotlinLogging
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.util.TablesNamesFinder

object MysqlIdentApp {
    private val UPDATE_SQL = "update\\s+([a-zA-Z_]+)".toRegex()
    private val logger = KotlinLogging.logger {}

    fun analysis(sql: String): SimpleRelation? {
        val table = SimpleRelation()

        try {
            val statement: Statement = CCJSqlParserUtil.parse(sql)
            val tablesNamesFinder = TablesNamesFinder()

            table.tableNames = tablesNamesFinder.getTableList(statement).map {
                var tableName = it
                if (it.startsWith("`") && it.endsWith("`")) {
                     tableName = tableName.removeSuffix("`").removePrefix("`")
                }

                tableName
            }
        } catch (e: Exception) {
            // try used regex to match for CRUD by tables
            if (UPDATE_SQL.find(sql) != null) {
                val tableName = UPDATE_SQL.find(sql)!!.groups[1]!!.value
                table.tableNames = arrayListOf(tableName)
                return table
            }

            logger.info("analysis failure for sql: $sql")
            return null
        }

        return table
    }
}