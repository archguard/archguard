package org.archguard.scanner.analyser.database

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.context.CodeDatabaseRelation

class GoSqlAnalyser {
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<CodeDatabaseRelation> {
        val relations: MutableList<CodeDatabaseRelation> = mutableListOf()

        node.Functions.map { codeFunction ->
            codeFunction.FunctionCalls.map { call ->
                if (call.NodeName.contains("sql.DB") || call.NodeName == "Dao.db") {
                    when (call.FunctionName) {
                        "Raw", "Query", "QueryRow", "Exec" -> {
                            val firstParameter = call.Parameters.firstOrNull()?.TypeValue ?: ""
                            val relation = analysisParams(firstParameter, node, codeFunction)
                            if (relation != null) {
                                relations += relation
                            } else {
                                val secondParameter = call.Parameters.getOrNull(1)?.TypeValue ?: ""
                                val second = analysisParams(secondParameter, node, codeFunction)
                                if (second != null) {
                                    relations += second
                                }
                            }
                        }
                    }
                }
            }
        }

        return relations
    }

    private fun analysisParams(
        parameter: String, node: CodeDataStruct, codeFunction: CodeFunction
    ): CodeDatabaseRelation? {
        if (parameter.contains("\"") && parameter.length > 10) {
            val sql = parameter.removeSurrounding("\"")
            return CodeDatabaseRelation(
                packageName = node.Package,
                className = node.NodeName,
                functionName = codeFunction.Name,
                tables = MysqlIdentApp.analysis(sql)?.tableNames ?: emptyList(),
                sqls = listOf(sql)
            )
        } else {
            codeFunction.LocalVariables.filter { it.TypeValue == parameter }.forEach {
                val sql = it.TypeType.removeSurrounding("\"")
                return CodeDatabaseRelation(
                    packageName = node.Package,
                    className = node.NodeName,
                    functionName = codeFunction.Name,
                    tables = MysqlIdentApp.analysis(sql)?.tableNames ?: emptyList(),
                    sqls = listOf(sql)
                )
            }
        }

        return null
    }
}