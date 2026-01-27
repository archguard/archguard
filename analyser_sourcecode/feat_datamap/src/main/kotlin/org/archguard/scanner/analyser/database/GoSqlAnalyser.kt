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
                // Check for database method calls on any object (sql.DB, Dao.db, or transaction objects like tx)
                if (call.NodeName.contains("sql.DB") || call.NodeName == "Dao.db" ||
                    call.FunctionName in listOf("Raw", "Query", "QueryRow", "Exec")) {
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
            // First try exact match
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

            // Handle complex expressions like fmt.Sprintf(_listCheckSQL, args) + " AND "
            // Extract SQL constant references (identifiers starting with _ and ending with SQL)
            val sqlConstantPattern = Regex("""_\w+SQL""")
            val matches = sqlConstantPattern.findAll(parameter)
            for (match in matches) {
                val constantName = match.value
                codeFunction.LocalVariables.filter { it.TypeValue == constantName }.forEach {
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