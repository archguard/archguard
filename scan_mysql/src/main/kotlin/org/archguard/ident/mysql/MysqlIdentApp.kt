package org.archguard.ident.mysql

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.util.TablesNamesFinder
import org.archguard.ident.mysql.model.SimpleRelation
import org.slf4j.LoggerFactory

object MysqlIdentApp {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val UPDATE_SQL = "update\\s+([a-zA-Z_]+)".toRegex()

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