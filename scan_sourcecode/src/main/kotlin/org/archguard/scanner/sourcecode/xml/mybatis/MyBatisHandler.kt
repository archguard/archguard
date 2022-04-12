package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.BuilderException
import org.apache.ibatis.builder.ParameterExpression
import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.parsing.GenericTokenParser
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.scripting.xmltags.*
import org.apache.ibatis.session.Configuration
import org.archguard.scanner.sourcecode.xml.BasedXmlHandler
import org.archguard.scanner.sourcecode.xml.XmlConfig
import org.w3c.dom.Node
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
            "include" -> {

            }
            "if" -> {

            }
            "choose", "when", "otherwise" -> {

            }
            "trim", "where", "set" -> {

            }
            "foreach" -> {
            }
            "bind" -> {

            }
            "sql" -> { // raw sqls

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

        val xPathParser = XPathParser(inputStream, true, config.variables, XMLMapperEntityResolver())
        val context = xPathParser.evalNode("/mapper")
        val list = context.evalNodes("select|insert|update|delete")


        list.forEach {
            val xmlScriptBuilder = XMLScriptBuilder(config, it)

            val nodes = parseDynamicTags(it)
//            nodes.forEach { node ->
//                val simpleName = node.javaClass.simpleName
//                println(simpleName)
//                when (simpleName) {
//                    "TextSqlNode" -> {
//                        val textSqlNode = node as TextSqlNode
//                        println(textSqlNode)
//                    }
//                    "StaticTextSqlNode" -> {
//                        val textSqlNode = node as StaticTextSqlNode
//                        println(textSqlNode)
//                    }
//                }
//            }
        }

        return this.config
    }

    fun parseDynamicTags(node: XNode): MutableList<SqlNode> {
        val contents: MutableList<SqlNode> = ArrayList()
        val children = node.node.childNodes
        for (i in 0 until children.length) {
            val child = node.newXNode(children.item(i))
            if (child.node.nodeType == Node.CDATA_SECTION_NODE || child.node.nodeType == Node.TEXT_NODE) {
                val data = child.getStringBody("")
                val textSqlNode = TextSqlNode(data)
                if (textSqlNode.isDynamic) {
                    contents.add(textSqlNode)
                } else {
                    contents.add(StaticTextSqlNode(data))
                }
            } else if (child.node.nodeType == Node.ELEMENT_NODE) { // issue #628
                // todo: parse text parameters from origin
                val data = child.getStringBody("")

                when (child.node.nodeName) {
                    "trim" -> {}
                    "where" -> {}
                    "set" -> {}
                    "foreach" -> {
                        val foreachNode = parseDynamicTags(child)
                        foreachNode[0]
//                        println(toSql(data))

                        val propertiesMap: Map<String, String> = ParameterExpression(data)
                        propertiesMap.forEach {
                            println("${it.key}: ${it.value}")
                        }
                    }
                    "if" -> {}
                    "choose" -> {}
                    "when" -> {}
                    "otherwise" -> {}
                    "bind" -> {}
                }

            }
        }

        return contents
    }

    private val itemIndex: String = ""
    private val index = 0
    fun toSql(sql: String): String? {
        var item = ""
        val parser = GenericTokenParser("#{", "}") { content: String ->
            var newContent = content.replaceFirst(
                "^\\s*$item(?![^.,:\\s])".toRegex(),
                itemizeItem(item, index)
            )
            if (itemIndex != null && newContent == content) {
                newContent = content.replaceFirst(
                    "^\\s*$itemIndex(?![^.,:\\s])".toRegex(),
                    itemizeItem(itemIndex, index)
                )
            }
            "#{$newContent}"
        }

        return parser.parse(sql)
    }


    private fun itemizeItem(item: String, i: Int): String {
        return ForEachSqlNode.ITEM_PREFIX + item + "_" + i
    }
}
