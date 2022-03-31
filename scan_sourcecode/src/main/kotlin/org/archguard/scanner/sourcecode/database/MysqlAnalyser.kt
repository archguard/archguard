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
                if(it.Name == "SqlQuery") {
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

            if(sqls.size > 0) {
                logs += SqlRecord (
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

    private fun sqlify(value: String): String {
        var text = removeBeginEndQuotes(value)
        text = removePlus(text)
        return text
    }

    private fun removePlus(text: String) = text.replace("\"+\"", "")
    private fun removeBeginEndQuotes(value: String) = value.removeSuffix("\"").removePrefix("\"")
}