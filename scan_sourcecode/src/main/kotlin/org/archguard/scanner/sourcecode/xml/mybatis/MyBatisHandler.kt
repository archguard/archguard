package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.xml.XMLMapperBuilder
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.session.Configuration
import org.archguard.scanner.sourcecode.xml.BasedXmlHandler
import org.archguard.scanner.sourcecode.xml.XmlConfig
import org.xml.sax.Attributes
import java.io.FileInputStream

class MybatisEntry(
    var namespace: String = "",
    // alias to method id
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
            "select", "update", "delete", "insert" -> {
                currentMapper.operationId = attributes.getValue("id")
            }
            else -> println(qName)
        }
    }

    override fun compute(filePath: String): XmlConfig {
        val inputStream = FileInputStream(filePath)

        val config = Configuration()
        config.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        config.isShrinkWhitespacesInSql = true

        val builder = XMLMapperBuilder(inputStream, config, filePath, config.sqlFragments)
        builder.parse()
        inputStream.close()

        // todo: typeAlias for fake data
        config.mappedStatements.forEach {
            if (it.sqlSource != null) {
//             println(it.sqlSource.getBoundSql('?').sql)
            }
        }

        return this.config
    }
}
