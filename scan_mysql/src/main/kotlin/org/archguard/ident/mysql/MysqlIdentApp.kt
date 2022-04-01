package org.archguard.ident.mysql

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.util.TablesNamesFinder
import org.archguard.ident.mysql.model.SimpleRelation
import org.slf4j.LoggerFactory

object MysqlIdentApp {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun analysis(sql: String): SimpleRelation? {
        val table = SimpleRelation()

        try {
            val statement: Statement = CCJSqlParserUtil.parse(sql)
            val selectStatement: Select = statement as Select
            val tablesNamesFinder = TablesNamesFinder()

            table.tableNames = tablesNamesFinder.getTableList(selectStatement).map {
                var tableName = it
                if (it.startsWith("`") && it.endsWith("`")) {
                     tableName = tableName.removeSuffix("`").removePrefix("`")
                }

                tableName
            }
        } catch (e: Exception) {
            logger.warn(e.toString())
            logger.info(sql)
            return null
        }

        return table
    }
}