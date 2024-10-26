package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.database.JvmSqlAnalyser
import org.archguard.scanner.analyser.xml.XmlParser
import org.archguard.context.NodeRelation
import org.archguard.scanner.core.diffchanges.NodeRelationBuilder
import org.archguard.context.CodeDatabaseRelation
import org.archguard.scanner.analyser.database.GoSqlAnalyser
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory
import java.io.File

class DataMapAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser, NodeRelationBuilder() {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(input: List<CodeDataStruct>): List<CodeDatabaseRelation> {
        val language = context.language.lowercase()
        val path = context.path

        val relations = when (language) {
            "java", "kotlin" -> {
                logger.info("start analysis database api ---- ${language.lowercase()}")
                this.fillFunctionMap(input)
                this.fillReverseCallMap(input)

                if (context.debug) logFunctionMapInfo()

                val sqlAnalyser = JvmSqlAnalyser()
                val records = input.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }
                val mybatisEntries = XmlParser.parseMybatis(path)
                val dbRelations = sqlAnalyser.convertMyBatis(mybatisEntries)

                val databaseRelations = dbRelations + records
                databaseRelations.map {
                    val changeRelations: MutableList<NodeRelation> = mutableListOf()
                    val callee = it.packageName + "." + it.className + "." + it.functionName

                    this.resetCount()
                    this.calculateReverseCalls(callee, changeRelations)

                    it.relations = changeRelations
                    it
                }

                databaseRelations
            }

            "go", "golang" -> {
                logger.info("start analysis database api ---- ${language.lowercase()}")
                val sqlAnalyser = GoSqlAnalyser()
                val records = input.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }

                records
            }

            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        client.saveRelation(relations)
        return relations
    }

    private fun logFunctionMapInfo() {
        // write function map to file
        val file = File("function_map.txt")
        this.functionMap.forEach { (k, v) ->
            file.appendText("$k: $v\n")
        }

        // write reverse call map to file
        val file2 = File("reverse_call_map.txt")
        this.reverseCallMap.forEach { (k, v) ->
            file2.appendText("$k: $v\n")
        }

        val file3 = File("injection_map.txt")
        this.injectionMap.forEach { (k, v) ->
            file3.appendText("$k: $v\n")
        }
    }
}
