package org.archguard.scanner.analyser.database

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.context.CodeDatabaseRelation

class GoSqlAnalyser {
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<CodeDatabaseRelation> {
        val relations: MutableList<CodeDatabaseRelation> = mutableListOf()

        node.Functions.map { codeFunction ->
            codeFunction.FunctionCalls.map { call ->
                if (call.NodeName.contains("sql.DB")) {
                    when (call.FunctionName) {
                        "Query" -> {
                            val parameter = call.Parameters.firstOrNull()?.TypeValue ?: ""
                            analysisParams(parameter, relations, node, codeFunction)
                        }

                        "QueryRow" -> {
                            val parameter = call.Parameters.getOrNull(1)?.TypeValue ?: ""
                            analysisParams(parameter, relations, node, codeFunction)
                        }
                    }
                }
            }
        }

        return relations
    }

    private fun analysisParams(
        parameter: String,
        relations: MutableList<CodeDatabaseRelation>,
        node: CodeDataStruct,
        codeFunction: CodeFunction
    ) {
        if (parameter.contains("\"") && parameter.length > 10) {
            relations.add(
                CodeDatabaseRelation(
                    packageName = node.Package,
                    className = node.NodeName,
                    functionName = codeFunction.Name,
                    sqls = listOf(parameter)
                )
            )
        } else {
            codeFunction.LocalVariables.filter { it.TypeValue == parameter }.forEach {
                relations.add(
                    CodeDatabaseRelation(
                        packageName = node.Package,
                        className = node.NodeName,
                        functionName = codeFunction.Name,
                        sqls = listOf(it.TypeType)
                    )
                )
            }
        }
    }
}