package org.archguard.scanner.analyser.database

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.context.CodeDatabaseRelation

class GoSqlAnalyser {
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<CodeDatabaseRelation> {
        val relations: MutableList<CodeDatabaseRelation> = mutableListOf()

        val packageName = node.FilePath.removePrefix(workspace).split("/")
            .dropLast(1).joinToString("/")

        node.Functions.map { codeFunction ->
            codeFunction.FunctionCalls.map { call ->
                if (call.NodeName.contains("sql.DB") || call.NodeName == "Dao.db") {
                    when (call.FunctionName) {
                        "Raw", "Query", "QueryRow", "Exec" -> {
                            val firstParameter = call.Parameters.firstOrNull()?.TypeValue ?: ""
                            val relation = analysisParams(firstParameter, node, codeFunction, packageName)
                            if (relation != null) {
                                relations += relation
                            } else {
                                val secondParameter = call.Parameters.getOrNull(1)?.TypeValue ?: ""
                                val second = analysisParams(secondParameter, node, codeFunction, packageName)
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
        parameter: String, node: CodeDataStruct, codeFunction: CodeFunction, packageName: String
    ): CodeDatabaseRelation? {
        if (parameter.contains("\"") && parameter.length > 10) {
            val sql = postFixSql(parameter.removeSurrounding("\""))
            val tables = MysqlIdentApp.analysis(sql)?.tableNames ?: emptyList()
            if (tables.isEmpty()) {
                return null
            }

            return CodeDatabaseRelation(
                packageName = packageName,
                className = node.NodeName,
                functionName = codeFunction.Name,
                tables = tables,
                sqls = listOf(sql)
            )
        } else {
            codeFunction.LocalVariables.filter { it.TypeValue == parameter }.forEach {
                val sql = postFixSql(it.TypeType.removeSurrounding("\""))
                val tables = MysqlIdentApp.analysis(sql)?.tableNames ?: emptyList()
                if (tables.isEmpty()) {
                    return null
                }
                return CodeDatabaseRelation(
                    packageName = packageName,
                    className = node.NodeName,
                    functionName = codeFunction.Name,
                    tables = tables,
                    sqls = listOf(sql)
                )
            }
        }

        return null
    }

    fun postFixSql(input: String): String {
        /// SELECT id, name, receivers, `interval`, ctime, mtime FROM `alert_group` WHERE id in (%s) AND is_deleted = 0
        /// should replace %s, %d, to mock value
        return input.replace("%s", "0")
            .replace("%02d", "0")
            .replace("%d", "0")
    }
}