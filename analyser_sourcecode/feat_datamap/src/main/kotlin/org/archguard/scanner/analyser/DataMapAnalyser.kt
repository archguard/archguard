package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.database.MysqlAnalyser
import org.archguard.scanner.analyser.xml.XmlParser
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.slf4j.LoggerFactory

class DataMapAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    private val client = context.client
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun analyse(input: List<CodeDataStruct>): List<CodeDatabaseRelation> {
        val language = context.language.lowercase()
        val path = context.path

        val relations = when (language) {
            "java", "kotlin" -> {
                logger.info("start analysis database api ---- ${language.lowercase()}")
                val sqlAnalyser = MysqlAnalyser()
                val records = input.flatMap { data ->
                    sqlAnalyser.analysisByNode(data, "")
                }
                val mybatisEntries = XmlParser.parseMybatis(path)
                val relations = sqlAnalyser.convertMyBatis(mybatisEntries)

                relations + records
            }
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        client.saveRelation(relations)
        return relations
    }
}
