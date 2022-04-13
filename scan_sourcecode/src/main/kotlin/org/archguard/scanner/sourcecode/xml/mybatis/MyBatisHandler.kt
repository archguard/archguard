package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.scripting.xmltags.*
import org.apache.ibatis.session.Configuration
import org.archguard.scanner.sourcecode.xml.BasedXmlHandler
import org.archguard.scanner.sourcecode.xml.XmlConfig
import org.w3c.dom.Node
import java.io.FileInputStream

class MybatisEntry(
    var namespace: String = "",
    var methodSqlMap: MutableMap<String, String> = mutableMapOf()
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

    override fun compute(filePath: String): XmlConfig {
        val inputStream = FileInputStream(filePath)

        streamToSqls(inputStream)

        return this.config
    }

    fun streamToSqls(inputStream: FileInputStream): MybatisEntry {
        val config = Configuration()
        config.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        config.isShrinkWhitespacesInSql = true

        val parser = XPathParser(inputStream, true, config.variables, XMLMapperEntityResolver())
        val context = parser.evalNode("/mapper")
        val namespace = context.getStringAttribute("namespace")

        val list = context.evalNodes("select|insert|update|delete")

        val entry = MybatisEntry(namespace)

        list.forEach {
            val methodName = it.getStringAttribute("id")
            val xmlScriptBuilder = XMLScriptBuilder(config, it)
            val sqlSource = xmlScriptBuilder.parseScriptNode()

            // if is a foreach
            val params = fakeParameters(it)

            val sqlString = sqlSource.getBoundSql(params).sql
            entry.methodSqlMap[methodName] = sqlString
        }

        return entry
    }

    private fun fakeParameters(node: XNode): MutableMap<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        val children = node.node.childNodes
        for (i in 0 until children.length) {
            val child = node.newXNode(children.item(i))
            if (child.node.nodeType == Node.CDATA_SECTION_NODE || child.node.nodeType == Node.TEXT_NODE) {
//                val data = child.getStringBody("")
            } else if (child.node.nodeType == Node.ELEMENT_NODE) { // issue #628
//                val data = child.getStringBody("")
                when (child.node.nodeName) {
                    "trim" -> {}
                    "where" -> {}
                    "set" -> {}
                    "foreach" -> {
                        val items = mutableListOf<String>()
                        items += "placeholder"
                        params["list"] = items
                    }
                    "if" -> {}
                    "choose" -> {}
                    "when" -> {}
                    "otherwise" -> {}
                    "bind" -> {}
                }

            }
        }

        return params
    }
}
