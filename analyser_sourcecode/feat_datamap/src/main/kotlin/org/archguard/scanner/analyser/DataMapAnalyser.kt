package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.database.JvmSqlAnalyser
import org.archguard.scanner.analyser.xml.XmlParser
import org.archguard.scanner.core.diffchanges.NodeRelation
import org.archguard.scanner.core.diffchanges.NodeRelationBuilder
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory

class DataMapAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val relationBuilder = object : NodeRelationBuilder() {
        fun initDataMap(ds: List<CodeDataStruct>) {
            this.fillFunctionMap(ds)
            this.fillReverseCallMap(ds)
        }
    }

    override fun analyse(input: List<CodeDataStruct>): List<CodeDatabaseRelation> {
        val language = context.language.lowercase()
        val path = context.path

        val relations = when (language) {
            "java", "kotlin" -> {
                logger.info("start analysis database api ---- ${language.lowercase()}")
                val sqlAnalyser = JvmSqlAnalyser()
                val records = input.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }
                val mybatisEntries = XmlParser.parseMybatis(path)
                val dbRelations = sqlAnalyser.convertMyBatis(mybatisEntries)

                val databaseRelations = dbRelations + records
                databaseRelations.forEach {
                    val changeRelations: MutableList<NodeRelation> = mutableListOf()
                    val callee = it.packageName + "." + it.className + "." + it.functionName
                    relationBuilder.calculateReverseCalls(callee, changeRelations)

                    it.relations = changeRelations
                }

                databaseRelations
            }

            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        client.saveRelation(relations)
        return relations
    }
}
