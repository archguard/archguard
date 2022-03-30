package org.archguard.scanner.sourcecode.database

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class MysqlLog(
    val Package: String = "",
    val ClassName: String = "",
    val FunctionName: String = "",
    val Sql: List<String> = listOf()
)

class MysqlAnalyser {
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<MysqlLog> {
        val logs: MutableList<MysqlLog> = mutableListOf()
        // by annotation: identify
        val sqls: MutableList<String> = mutableListOf()
        node.Functions.forEach { function ->
            println(Json.encodeToString(function.Annotations))
            function.Annotations.forEach {
                if(it.Name == "SqlQuery") {
                    val value = it.KeyValues[0].Value
                    sqls += value.removeSuffix("\"").removePrefix("\"")
                }
            }

            if(sqls.size > 0) {
                logs += MysqlLog (
                    Package = node.Package,
                    ClassName = node.NodeName,
                    FunctionName = function.Name,
                    Sql = sqls
                )
            }
        }

        // by call: identify by jdbi

        return logs
    }
}