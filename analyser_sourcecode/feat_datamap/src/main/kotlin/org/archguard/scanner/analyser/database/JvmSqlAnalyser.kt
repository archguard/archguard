package org.archguard.scanner.analyser.database

import chapi.domain.core.CodeDataStruct
import org.archguard.context.CodeDatabaseRelation
import org.archguard.scanner.analyser.xml.mybatis.MybatisEntry
import org.slf4j.LoggerFactory

class JvmSqlAnalyser {
    private val logger = LoggerFactory.getLogger(javaClass)

    // todo: split by framework
    fun analysisByNode(node: CodeDataStruct, workspace: String): MutableList<CodeDatabaseRelation> {
        val relations: MutableList<CodeDatabaseRelation> = mutableListOf()
        val hasRepositoryAnnotation = node.Annotations.any { it.Name == "Repository" }
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

            val implementations: MutableList<String> = mutableListOf()

            if ((sqls.size > 0 || hasRepositoryAnnotation)) {
                relations += CodeDatabaseRelation(
                    packageName = node.Package,
                    className = node.NodeName,
                    functionName = function.Name,
                    tables = tables.toList(),
                    implementations = implementations,
                    sqls = sqls
                )

                if (implementations.isNotEmpty()) {
                    logger.info("found implementation for ${node.NodeName} -> ${implementations}")
                    val firstImpl = implementations.first()
                    val stringList = firstImpl.split(".")
                    val packageName = stringList.dropLast(1).joinToString(".")
                    val className = stringList.last()

                    relations += CodeDatabaseRelation(
                        packageName = packageName,
                        className = className,
                        functionName = function.Name,
                        tables = tables.toList(),
                        implementations = implementations,
                        sqls = sqls
                    )
                }
            }
        }

        // by call: identify by jdbi
        return relations
    }

    fun convertMyBatis(mybatisEntries: List<MybatisEntry>): MutableList<CodeDatabaseRelation> {
        return mybatisEntries.flatMap { entry ->
            val splits = entry.namespace.split(".")
            val className = splits.last()
            val packageName = splits.dropLast(1).joinToString(".")

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
}
