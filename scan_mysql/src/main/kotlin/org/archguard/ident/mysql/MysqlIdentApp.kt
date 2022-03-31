package org.archguard.ident.mysql

import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.Select
import net.sf.jsqlparser.util.TablesNamesFinder
import org.archguard.ident.mysql.model.SimpleRelation

object MysqlIdentApp {
    fun analysis(sql: String): SimpleRelation? {
        val table = SimpleRelation()

        try {
            val statement: Statement = CCJSqlParserUtil.parse(sql)
            val selectStatement: Select = statement as Select
            val tablesNamesFinder = TablesNamesFinder()

            table.tableNames = tablesNamesFinder.getTableList(selectStatement)
        } catch (e: Exception) {
            println(e)
            return null
        }

        return table
    }
}