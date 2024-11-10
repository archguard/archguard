package org.archguard.scanner.analyser.database

import mu.KotlinLogging
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.util.TablesNamesFinder

object MysqlIdentApp {
    private val UPDATE_SQL = "update\\s+([a-zA-Z_]+)".toRegex()
    private val SELECT_FROM_SQL = "select\\s+.*\\s+from\\s+([a-zA-Z_]+)".toRegex()
    private val logger = KotlinLogging.logger {}

    fun analysis(input: String): SimpleDbStructure? {
        val table = SimpleDbStructure()
        val sql = preHandleSql(input)

        try {
            val statement: Statement = CCJSqlParserUtil.parse(sql)
            val tablesNamesFinder = TablesNamesFinder()

            table.tableNames = tablesNamesFinder.getTableList(statement).map {
                it.removeSurrounding("`")
            }
        } catch (e: Exception) {
            // try used regex to match for CRUD by tables
            if (UPDATE_SQL.find(sql) != null) {
                val tableName = UPDATE_SQL.find(sql)!!.groups[1]!!.value
                table.tableNames = arrayListOf(tableName)
                return table
            }

            if (SELECT_FROM_SQL.find(sql) != null) {
                val tableName = SELECT_FROM_SQL.find(sql)!!.groups[1]!!.value
                table.tableNames = arrayListOf(tableName)
                return table
            }

            logger.info("analysis failure for sql: $sql")
            return null
        }

        return table
    }

    private fun preHandleSql(input: String): String {
        // handle for fmt.Print string, like
        // ```
        // "SELECT id,title,sub_title,logo FROM es_teams WHERE is_deleted=0 AND id in (%s)", xstr.JoinInts(tids)
        // ```
        // if stars with ", substring before last ", and remove the first and last "
        var sql = input.trim()
        if (sql.startsWith("\"")) {
            sql = sql.substring(1, sql.lastIndexOf("\""))
            // replace %s to mock value
        }

        /**
         * handle for end with ", remove the last two "
         * ```
         * SELECT COUNT(*) FROM vip_price_config_v2 WHERE platform = ? AND month = ? AND sub_type = ? AND suit_type = ? %s;", "
         * ```
         * if ends with ", remove the last ", and last " also
         */
        if (sql.endsWith("\"")) {
            sql = sql.removeSuffix("\"")
            sql = sql.substring(0, sql.lastIndexOf("\""))
        }
        return sql
    }
}