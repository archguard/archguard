package org.archguard.scanner.analyser.database

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.analyser.xml.mybatis.MybatisEntry
import org.slf4j.LoggerFactory

class JvmSqlAnalyser {
    private val logger = LoggerFactory.getLogger(javaClass)

    // todo: split by framework
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<CodeDatabaseRelation> {
        val relations: MutableList<CodeDatabaseRelation> = mutableListOf()
        // by annotation: identify
        node.Functions.forEach { function ->
            val sqls: MutableList<String> = mutableListOf()
            val tables: MutableSet<String> = mutableSetOf()

            function.Annotations.forEach {
                // jpa use `@Query`, jdbi use `SqlQuery`
                if ((it.Name == "Query" || it.Name == "SqlQuery") && it.KeyValues.isNotEmpty()) {
                    val originSql = it.KeyValues[0].Value
                    val pureValue = sqlify(originSql)
                    if (MysqlIdentApp.analysis(pureValue) != null) {
                        tables += MysqlIdentApp.analysis(pureValue)!!.tableNames
                    } else {
                        logger.warn("error for ${node.NodeName}.${function.Name} origin:$originSql \nnew:$pureValue")
                    }

                    sqls += pureValue
                }
            }

            // try to catch in function call
            function.FunctionCalls.forEach {
                val callMethodName = it.FunctionName.split(".").last()
                if (callMethodName == "createQuery" && it.Parameters.isNotEmpty()) {
                    val originSql = it.Parameters[0].TypeValue
                    val pureValue = sqlify(originSql)
                    if (MysqlIdentApp.analysis(pureValue) != null) {
                        tables += MysqlIdentApp.analysis(pureValue)!!.tableNames
                    } else {
                        logger.warn("error for ${node.NodeName}.${function.Name} origin:$originSql\nnew:$pureValue")
                    }

                    sqls += pureValue
                }
            }

            if (sqls.size > 0) {
                relations += CodeDatabaseRelation(
                    packageName = node.Package,
                    className = node.NodeName,
                    functionName = function.Name,
                    tables = tables.toList(),
                    sqls = sqls
                )
            }
        }

        // by call: identify by jdbi
        return relations
    }

    fun convertMyBatis(mybatisEntries: List<MybatisEntry>): MutableList<CodeDatabaseRelation> {
        return mybatisEntries.flatMap { entry ->
            val splits = entry.namespace.split(".")
            val className = splits.last()
            splits.dropLast(1)
            val packageName = splits.joinToString(".")

            entry.methodSqlMap.map {
                val tables = MysqlIdentApp.analysis(it.value)?.tableNames
                CodeDatabaseRelation(
                    packageName = packageName,
                    className = className,
                    functionName = it.key,
                    tables = tables?.toList() ?: listOf(),
                    sqls = listOf(it.value)
                )
            }
        }.toMutableList()
    }

    fun sqlify(value: String): String {
        var text = handleRawString(value)
        text = removeBeginEndQuotes(text)

        // handle for variable
        text = removeVariableInLine(text)
        text = removeKotlinVariable(text)
        text = removeJdbiValueBind(text)
        text = removeEndWithMultipleSingleQuote(text)

        text = removeNextLine(text)
        // " " + module
        text = removePlusWithVariable(text)
        // " " + " "
        text = removePlusSymbol(text)
        text = processIn(text)

        // sql fixed
        text = fillPagination(text)
        return text
    }

    private fun fillPagination(text: String): String {
        var sqlText = text

        // fill lost offset offset
        sqlText = sqlText
            .replace("offset ''", "offset 10")
            .replace("OFFSET ''", "OFFSET 10")

        // fill lost limit variable
        sqlText = sqlText
            .replace("limit ''", "limit 10")
            .replace("LIMIT ''", "LIMIT 10")

        return sqlText
    }

    private val RAW_STRING_REGEX = "\"\"\"(((.*?)|(\r\n|\n))+)\"\"\"".toRegex()
    private fun handleRawString(text: String): String {
        val rawString = RAW_STRING_REGEX.find(text) ?: return text

        return rawString.groups[1]!!.value
    }

    // some text: "\"+orderSqlPiece+\""
    private val VARIABLE_IN_LINE = "(\"\\\\\"\\+[a-zA-Z0-9_]+\\+\"\\\\\")".toRegex()
    private fun removeVariableInLine(text: String): String {
        val find = VARIABLE_IN_LINE.find(text) ?: return text

        return text.replace(VARIABLE_IN_LINE, "*")
    }

    private val IN_REGEX = "in\\s+\\((\\s+)?<([a-zA-Z0-9_]+)>(\\s+)?\\)".toRegex()
    private fun processIn(text: String): String {
        val find = IN_REGEX.find(text) ?: return text

        return text.replace(IN_REGEX, "in (:${find.groups[2]!!.value})")
    }

    // example: `where system_id=:systemId ` => `where system_id=''`
    private val JDBI_VALUE_BIND = ":([a-zA-Z0-9_]+)".toRegex()
    private fun removeJdbiValueBind(text: String): String {
        val find = JDBI_VALUE_BIND.find(text) ?: return text
        return text.replace(JDBI_VALUE_BIND, "''")
    }

    private val KOTLIN_VARIABLE_WITH_QUOTE = "'\\\$([a-zA-Z0-9_]+)'".toRegex()
    private val KOTLIN_VARIABLE = "\\\$([a-zA-Z0-9_]+)".toRegex()
    private fun removeKotlinVariable(text: String): String {
        var str = text
        val find = KOTLIN_VARIABLE_WITH_QUOTE.find(str)
        if (find != null) {
            str = str.replace(KOTLIN_VARIABLE_WITH_QUOTE, "''")
        }

        val varFind = KOTLIN_VARIABLE.find(str)
        if (varFind != null) {
            str = str.replace(KOTLIN_VARIABLE, "''")
        }

        return str
    }

    private fun removeNextLine(text: String) = text
        .replace("\r\n", "")
        .replace("\n", "")

    private fun removePlusSymbol(text: String) = text
        .replace("\"+\"", "")
        .replace("+\"", "")

    private fun removePlusWithVariable(text: String) = text.replace("\"\\+([a-zA-Z0-9_]+)".toRegex(), "")
    private fun removeEndWithMultipleSingleQuote(text: String) = text.replace("\'\'\\s+\'\'".toRegex(), "''")
    private fun removeBeginEndQuotes(value: String): String {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.removeSuffix("\"").removePrefix("\"")
        }
        return value
    }

    private fun fillLimitEmpty(value: String) = value
        .replace("offset ''", "offset 10")
        .replace("OFFSET ''", "OFFSET 10")

    private fun fillOffsetEmpty(value: String) = value
        .replace("limit ''", "limit 10")
        .replace("LIMIT ''", "LIMIT 10")
}
