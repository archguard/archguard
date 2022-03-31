package org.archguard.scanner.sourcecode.database

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.ident.mysql.MysqlIdentApp

@Serializable
data class SqlRecord(
    val Package: String = "",
    val ClassName: String = "",
    val FunctionName: String = "",
    val Tables: List<String> = listOf(),
    val Sql: List<String> = listOf()
)

class MysqlAnalyser {
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<SqlRecord> {
        val logs: MutableList<SqlRecord> = mutableListOf()
        // by annotation: identify
        val sqls: MutableList<String> = mutableListOf()
        node.Functions.forEach { function ->
            val tables: MutableList<String> = mutableListOf()

            function.Annotations.forEach {
                if (it.Name == "SqlQuery") {
                    val pureValue = sqlify(it.KeyValues[0].Value)
                    if (MysqlIdentApp.analysis(pureValue) != null) {
                        tables += MysqlIdentApp.analysis(pureValue)!!.tableNames
                    }
                    sqls += pureValue
                }
            }

            function.FunctionCalls.forEach {
                val callMethodName = it.FunctionName.split(".").last()
                if (callMethodName == "createQuery") {
                    val pureValue = sqlify(it.Parameters[0].TypeValue)
                    if (MysqlIdentApp.analysis(pureValue) != null) {
                        tables += MysqlIdentApp.analysis(pureValue)!!.tableNames
                    }
                    sqls += pureValue
                }
            }

            if (sqls.size > 0) {
                logs += SqlRecord(
                    Package = node.Package,
                    ClassName = node.NodeName,
                    FunctionName = function.Name,
                    Tables = tables,
                    Sql = sqls
                )
            }
        }

        // by call: identify by jdbi

        return logs
    }

    fun sqlify(value: String): String {
        var text = handleRawString(value)
        text = removeBeginEndQuotes(text)
        text = removePlus(text)
        text = processIn(text)
        return text
    }

    private val RAW_STRING_REGEX = "\"\"\"(((.*?)|\n)+)\"\"\"".toRegex()

    private fun handleRawString(text: String): String {
        val rawString = RAW_STRING_REGEX.find(text)
        if(rawString != null) {
            val replace = rawString.groups[1]!!.value
            return replace
        }

        return text
    }

    private val IN_REGEX = "in\\s+\\((\\s+)?<([a-zA-Z]+)>(\\s+)?\\)".toRegex()

    private fun processIn(text: String): String {
        val find = IN_REGEX.find(text)
        if (find != null) {
            return text.replace(IN_REGEX, "in (:${find.groups[2]!!.value})")
        }
        return text
    }

    private fun removePlus(text: String) = text.replace("\"+\"", "")
    private fun removeBeginEndQuotes(value: String) = value.removeSuffix("\"").removePrefix("\"")
}