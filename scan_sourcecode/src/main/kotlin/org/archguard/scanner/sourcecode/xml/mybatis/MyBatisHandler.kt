package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.MapperBuilderAssistant
import org.apache.ibatis.builder.xml.XMLIncludeTransformer
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.mapping.ParameterMap
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder
import org.apache.ibatis.session.Configuration
import org.archguard.scanner.sourcecode.xml.BasedXmlHandler
import org.slf4j.LoggerFactory
import org.w3c.dom.Node
import java.io.FileInputStream

class MybatisEntry(
    var namespace: String = "",
    var methodSqlMap: MutableMap<String, String> = mutableMapOf()
)

class MyBatisHandler : BasedXmlHandler() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun name(): String {
        return "MyBatisHandler"
    }

    override fun detect(name: String?, publicId: String?, systemId: String?): Boolean {
        if (name == "mapper" && systemId == "http://mybatis.org/dtd/mybatis-3-mapper.dtd") {
            return true
        }

        return false
    }

    fun compute(filePath: String): MybatisEntry {
        val inputStream = FileInputStream(filePath)

        return streamToSqls(inputStream, filePath)
    }

    fun streamToSqls(inputStream: FileInputStream, resource: String): MybatisEntry {
        val configuration = Configuration()
        configuration.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        configuration.isShrinkWhitespacesInSql = true

//        val builder = XMLMapperBuilder(inputStream, configuration, resource, configuration.sqlFragments)
//        builder.parse()
//        inputStream.close()

        val parser = XPathParser(inputStream, true, configuration.variables, XMLMapperEntityResolver())
        val context = parser.evalNode("/mapper")
        val namespace = context.getStringAttribute("namespace")

        // alias to configurationElement
        val builderAssistant = MapperBuilderAssistant(configuration, resource)

        // todo: add sql element
        val sqlNodes = context.evalNodes("/mapper/sql")
        val basedParameters: MutableMap<String, Any> = mutableMapOf()
        sqlNodes.forEach {
            basedParameters += fakeParameters(it)
            var id = it.getStringAttribute("id")
            id = builderAssistant.applyCurrentNamespace(id, false)
            configuration.sqlFragments[id] = it
        }

        val list = context.evalNodes("select|insert|update|delete")

        val entry = MybatisEntry(namespace)

        list.forEach {
            try {
                val methodName = it.getStringAttribute("id")
                // Include Fragments before parsing
                val includeParser = XMLIncludeTransformer(configuration, builderAssistant)
                includeParser.applyIncludes(it.node)

                val xmlScriptBuilder = XMLScriptBuilder(configuration, it)
                val sqlSource = xmlScriptBuilder.parseScriptNode()

                // if is a foreach
                val params = basedParameters + fakeParameters(it)

                val sqlString = sqlSource.getBoundSql(params).sql
                entry.methodSqlMap[methodName] = sqlString
            } catch (e: Exception) {
                logger.info("process: $resource error")
                logger.info(e.toString())
            }
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
                    "where" -> {
                        params += fakeParameters(child)
                    }
                    "set" -> {
                        params += fakeParameters(child)
                    }
                    "foreach" -> {
                        val collection = child.getStringAttribute("collection") ?: "list"
                        val collectionItem = child.getStringAttribute("item") ?: "list"
                        val items = mutableListOf("placeholder")

                        if (collection.contains(".")) {
                            // todo: check need to support for multiple parents if exists
                            val parent = collection.split(".")[0]
                            params[parent] = items
                        }

                        params[collection] = items
                        params[collectionItem] = items
                    }
                    "if" -> {
                        val condition = child.getStringAttribute("test")
                        println(condition)
                    }
                    else -> {
                        println("Mybatis - need to support: ${child.node.nodeName}")
                    }
                }

            }
        }

        return params
    }
}
