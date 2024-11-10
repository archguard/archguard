package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.context.CodeDatabaseRelation
import org.archguard.context.NodeRelation
import org.archguard.scanner.analyser.database.GoSqlAnalyser
import org.archguard.scanner.analyser.database.JvmSqlAnalyser
import org.archguard.scanner.analyser.xml.XmlParser
import org.archguard.scanner.core.diffchanges.NodeRelationBuilder
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
                /// todo: spike for function map
                this.fillFunctionMap(input)
                this.fillReverseCallMap(input)
                if (context.debug) logFunctionMapInfo()

                logger.info("start analysis database api ---- ${language.lowercase()}")
                val sqlAnalyser = GoSqlAnalyser()
                val databaseRelations = input.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }

                databaseRelations
            }

            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        client.saveRelation(relations)
        return relations
    }

    /**
     * Logs the information of the function maps to separate text files.
     * This method writes the contents of three different maps - `functionMap`, `reverseCallMap`, and `injectionMap` to corresponding text files.
     * The purpose of this method is to provide a log of the current state or configuration of the maps, which can be useful for debugging or auditing.
     *
     * The `functionMap` is logged to a file named "function_map.txt". Each entry in the map is written in the format of "key: value" followed by a new line.
     *
     * Similarly, the `reverseCallMap` is logged to a file named "reverse_call_map.txt", and the `injectionMap` is logged to a file named "injection_map.txt".
     * Both files also contain entries in the same "key: value" format.
     *
     * This method assumes that the maps and the `File` objects are accessible in the context where it is called.
     * It does not return any value and directly appends the information to the files.
     */
    private fun logFunctionMapInfo() {
        val file = File("function_map.txt")
        this.functionMap.forEach { (k, v) ->
            file.appendText("$k: $v\n")
        }

        val inverseCallList = File("reverse_call_map.txt")
        this.reverseCallMap.forEach { (k, v) ->
            inverseCallList.appendText("$k: $v\n")
        }

        val injectionMap = File("injection_map.txt")
        this.injectionMap.forEach { (k, v) ->
            injectionMap.appendText("$k: $v\n")
        }
    }
}
