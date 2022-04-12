package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.builder.xml.XMLMapperBuilder
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.session.Configuration
import org.archguard.scanner.sourcecode.xml.BasedXmlHandler
import org.archguard.scanner.sourcecode.xml.XmlConfig
import org.xml.sax.Attributes
import java.io.FileInputStream

class MybatisEntry(
    var namespace: String = "",
    var operationId: String = ""
)

class MyBatisHandler : BasedXmlHandler() {
    private val config: MyBatisXmlConfig = MyBatisXmlConfig()
    private val currentMapper: MybatisEntry = MybatisEntry()

    override fun name(): String {
        return "MyBatisHandler"
    }

    override fun detect(name: String?, publicId: String?, systemId: String?): Boolean {
        if (name == "mapper" && systemId == "http://mybatis.org/dtd/mybatis-3-mapper.dtd") {
            return true
        }

        return false
    }

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (attributes == null) return

        when (qName) {
            "mapper" -> {
                currentMapper.namespace = attributes.getValue("namespace")
            }
            "insert" -> {
                currentMapper.operationId = attributes.getValue("id")
            }
            else -> println(qName)
        }
    }

    override fun compute(filePath: String): XmlConfig {
        val inputStream = FileInputStream(filePath)
//        val languageDriver: LanguageDriver = XMLLanguageDriver()

        val configuration = Configuration()
        configuration.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        configuration.isShrinkWhitespacesInSql = true

        val builder = XMLMapperBuilder(inputStream, configuration, filePath, configuration.sqlFragments)
        builder.parse()
        inputStream.close()

//        val sqlSource: SqlSource = sqlSourceBuilder.parse(sql, null, null)
//        val boundSql = sqlSource.getBoundSql(null)
//        val actual = boundSql.sql

        // todo: typeAlias for fake data

        configuration.mappedStatements.forEach {
            if (it.sqlSource != null) {
//             println(it.sqlSource.getBoundSql('?').sql)
            }
        }

        return this.config
    }
}
