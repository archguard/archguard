package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.MapperBuilderAssistant
import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.builder.xml.XMLIncludeTransformer
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.executor.keygen.NoKeyGenerator
import org.apache.ibatis.executor.keygen.SelectKeyGenerator
import org.apache.ibatis.mapping.*
import org.apache.ibatis.ognl.ComparisonExpression
import org.apache.ibatis.ognl.Ognl
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.scripting.LanguageDriver
import org.apache.ibatis.scripting.xmltags.DynamicContext
import org.apache.ibatis.scripting.xmltags.MixedSqlNode
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
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

class SimpleScriptBuilder(configuration: Configuration, val context: XNode) : XMLScriptBuilder(configuration, context) {
    fun getNode(): MixedSqlNode? {
        return parseDynamicTags(context)
    }
}

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
        val configuration = createConfiguration()
        val langDriver: LanguageDriver = XMLLanguageDriver()

        val parser = XPathParser(inputStream, true, configuration.variables, XMLMapperEntityResolver())
        val context = parser.evalNode("/mapper")

        val namespace = context.getStringAttribute("namespace")
        val entry = MybatisEntry(namespace)

        val builderAssistant = MapperBuilderAssistant(configuration, resource)
        val basedParameters: MutableMap<String, Any> = mutableMapOf()

        // create inside <sql> for inline
        val sqlNodes = context.evalNodes("/mapper/sql")
        parseSqlStatement(sqlNodes, basedParameters, builderAssistant, configuration)

        val crudList = context.evalNodes("select|insert|update|delete")
        crudList.forEach {
            val methodName = it.getStringAttribute("id")
            val params = basedParameters + fakeParameters(it)

            // 1. enable include
            val includeParser = XMLIncludeTransformer(configuration, builderAssistant)
            includeParser.applyIncludes(it.node)

            // 2. follow parseStatementNode to remove all keys
            val selectKeyNodes = it.evalNodes("selectKey")
            parseSelectKeyStatement(selectKeyNodes, methodName, langDriver, configuration, builderAssistant)

            // 3. get rootNode to do some simple calculate
            val rootNode: MixedSqlNode = SimpleScriptBuilder(configuration, it).getNode()!!
            val dynamicContext = DynamicContext(configuration, params)

            // every node apply in here
            try {
                rootNode.apply(dynamicContext)
            } catch (e: Exception) {
                // ignore this exception log, because ognl will always run parserExpression
            }

            // 4. convert to source. it will replace all parameters => ?
            val sqlSourceParser = SqlSourceBuilder(configuration)
            val sqlSource = sqlSourceParser.parse(dynamicContext.sql, Any::class.java, dynamicContext.bindings)
            val boundSql = sqlSource.getBoundSql(params)

            entry.methodSqlMap[methodName] = boundSql.sql
        }

        return entry
    }

    private fun parseSqlStatement(
        sqlNodes: MutableList<XNode>,
        basedParameters: MutableMap<String, Any>,
        builderAssistant: MapperBuilderAssistant,
        configuration: Configuration
    ) {
        sqlNodes.forEach {
            basedParameters += fakeParameters(it)
            var id = it.getStringAttribute("id")
            id = builderAssistant.applyCurrentNamespace(id, false)
            configuration.sqlFragments[id] = it
        }
    }

    private fun parseSelectKeyStatement(
        selectKeyNodes: MutableList<XNode>,
        methodName: String,
        langDriver: LanguageDriver,
        configuration: Configuration,
        builderAssistant: MapperBuilderAssistant
    ) {
        selectKeyNodes.forEach { selectNode ->
            val id: String = methodName + SelectKeyGenerator.SELECT_KEY_SUFFIX
            parseSelectKeyNode(id, selectNode, Any::class.java, langDriver, "", configuration, builderAssistant)
        }
        // remove before parser
        for (nodeToHandle in selectKeyNodes) {
            nodeToHandle.parent.node.removeChild(nodeToHandle.node)
        }
    }

    private fun createConfiguration(): Configuration {
        val configuration = Configuration()
        configuration.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        configuration.isShrinkWhitespacesInSql = true
        return configuration
    }

    private fun parseSelectKeyNode(
        id: String,
        nodeToHandle: XNode,
        parameterTypeClass: Class<*>,
        langDriver: LanguageDriver,
        databaseId: String,
        configuration: Configuration,
        builderAssistant: MapperBuilderAssistant
    ) {
        val statementType =
            StatementType.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()))
        val keyProperty = nodeToHandle.getStringAttribute("keyProperty")
        val keyColumn = nodeToHandle.getStringAttribute("keyColumn")
        val executeBefore = "BEFORE" == nodeToHandle.getStringAttribute("order", "AFTER")

        val sqlSource = langDriver.createSqlSource(configuration, nodeToHandle, parameterTypeClass)

        builderAssistant.addMappedStatement(
            id, sqlSource, statementType, SqlCommandType.SELECT,
            null as Int?, null as Int?, null as String?, parameterTypeClass, null as String?, null,
            null as ResultSetType?, false, false, false,
            NoKeyGenerator.INSTANCE, keyProperty, keyColumn, databaseId, langDriver, null
        )

        val id = builderAssistant.applyCurrentNamespace(id, false)
        val keyStatement: MappedStatement = configuration.getMappedStatement(id, false)
        configuration.addKeyGenerator(id, SelectKeyGenerator(keyStatement, executeBefore))
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
                    "trim",
                    "where" -> {
                        params += fakeParameters(child)
                    }
                    "set" -> {
                        params += fakeParameters(child)
                    }
                    "foreach" -> {
                        val collection = child.getStringAttribute("collection") ?: "list"
                        val collectionItem = child.getStringAttribute("item") ?: "list"
                        val items = mutableListOf(Any())

                        if (collection.contains(".")) {
                            // todo: check need to support for multiple parents if exists
                            val parent = collection.split(".")[0]
                            params[parent] = mutableListOf(mutableMapOf<String, Any>())
                        }

                        params[collection] = items
                        params[collectionItem] = items
                    }
                    "if" -> {
                        val condition = child.getStringAttribute("test")
                        val parseExpression = Ognl.parseExpression(condition)
                        val items = mutableListOf(Any())

                        when (parseExpression.javaClass.simpleName) {
                            "ASTEq", "ASTGreater", "ASTGreaterEq", "ASTLess", "ASTLessEq", "ASTNotEq" -> {
                                val ast = parseExpression as ComparisonExpression
                                for (i in 0 until ast.jjtGetNumChildren()) {
                                    val jjtGetChild = ast.jjtGetChild(i).toString()
                                    if (jjtGetChild != "null") {
                                        if (jjtGetChild.contains(".")) {
                                            // todo: check need to support for multiple parents if exists
                                            val split = jjtGetChild.split(".")
                                            val parent = split[0]
                                            params[parent] = mutableListOf(mutableMapOf<String, Any>())
                                        }

                                        params[jjtGetChild] = items
                                    }
                                }
                            }
                        }
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
