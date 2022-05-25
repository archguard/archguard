package org.archguard.scanner.analyser.xml.mybatis

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
import org.archguard.scanner.analyser.xml.BasedXmlHandler
import org.slf4j.LoggerFactory
import org.w3c.dom.Node
import java.io.File

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
        if (systemId != null) {
            if (name == "mapper" && (systemId.endsWith("mybatis-3-mapper.dtd"))) {
                return true
            }
        }

        return false
    }

    fun compute(filePath: String): MybatisEntry {
        logger.info("process file: $filePath")
        return streamToSqls(filePath)
    }

    fun streamToSqls(resource: String): MybatisEntry {
        val configuration = createConfiguration()

        var xml = File(resource).readText()
        xml = xml.replace("http://mybatis.org/dtd/mybatis-3-mapper.dtd", "classpath:/mybatis-3-mapper.dtd")
        xml = xml.replace("http://mybatis.org/dtd/mybatis-3-config.dtd", "classpath:/mybatis-3-config.dtd")

        val parser = XPathParser(xml, true, configuration.variables, XMLMapperEntityResolver())
        val context = parser.evalNode("/mapper")

        val mybatisEntry = MybatisEntry(context.getStringAttribute("namespace"))

        val builderAssistant = MapperBuilderAssistant(configuration, resource)
        val basedParameters: MutableMap<String, Any> = mutableMapOf()

        // create inside <sql> for inline
        val sqlNodes = context.evalNodes("/mapper/sql")
        parseSqlStatement(sqlNodes, basedParameters, builderAssistant, configuration)

        try {
            mybatisEntry.methodSqlMap = buildCrudSqlMap(context, basedParameters, configuration, builderAssistant)
        } catch (e: Exception) {
            logger.info(e.toString())
        }

        return mybatisEntry
    }

    private fun buildCrudSqlMap(
        context: XNode,
        basedParameters: MutableMap<String, Any>,
        configuration: Configuration,
        builderAssistant: MapperBuilderAssistant,
    ): MutableMap<String, String> {
        val sqlMap: MutableMap<String, String> = mutableMapOf()
        val langDriver: LanguageDriver = XMLLanguageDriver()
        val crudList = context.evalNodes("select|insert|update|delete")

        for (it in crudList) {
            val methodName = it.getStringAttribute("id")
            val params = basedParameters + fakeParameters(it)

            // 1. enable include
            try {
                val includeParser = XMLIncludeTransformer(configuration, builderAssistant)
                includeParser.applyIncludes(it.node)
            } catch (e: Exception) {
                logger.info(e.toString())
                continue
            }

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

            sqlMap[methodName] = boundSql.sql
        }
        return sqlMap
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

        val idWithNamespace = builderAssistant.applyCurrentNamespace(id, false)
        val keyStatement: MappedStatement = configuration.getMappedStatement(idWithNamespace, false)
        configuration.addKeyGenerator(idWithNamespace, SelectKeyGenerator(keyStatement, executeBefore))
    }


    private fun fakeParameters(node: XNode): MutableMap<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        val children = node.node.childNodes
        for (i in 0 until children.length) {
            val child = node.newXNode(children.item(i))
            if (child.node.nodeType == Node.CDATA_SECTION_NODE || child.node.nodeType == Node.TEXT_NODE) {
//                val data = child.getStringBody("")
            } else if (child.node.nodeType == Node.ELEMENT_NODE) {
                when (child.node.nodeName) {
                    "trim",
                    "where" -> {
                        params += fakeParameters(child)
                    }
                    "set" -> {
                        params += fakeParameters(child)
                    }
                    "foreach" -> {
                        processForeachParams(child, params)
                    }
                    "if" -> {
                        processIfNodeParams(child, params)
                    }
                    else -> {
                        println("Mybatis - need to support: ${child.node.nodeName}")
                    }
                }

            }
        }

        return params
    }

    private fun processForeachParams(
        child: XNode,
        params: MutableMap<String, Any>
    ) {
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

    private fun processIfNodeParams(
        child: XNode,
        params: MutableMap<String, Any>
    ) {
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
}
