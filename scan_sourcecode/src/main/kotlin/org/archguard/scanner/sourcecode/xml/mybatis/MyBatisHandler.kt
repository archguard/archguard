package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.builder.MapperBuilderAssistant
import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.builder.xml.XMLIncludeTransformer
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.executor.keygen.NoKeyGenerator
import org.apache.ibatis.executor.keygen.SelectKeyGenerator
import org.apache.ibatis.mapping.*
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
        val configuration = Configuration()
        configuration.defaultResultSetType = ResultSetType.SCROLL_INSENSITIVE
        configuration.isShrinkWhitespacesInSql = true

        val parser = XPathParser(inputStream, true, configuration.variables, XMLMapperEntityResolver())
        val context = parser.evalNode("/mapper")
        val namespace = context.getStringAttribute("namespace")

        // alias to configurationElement
        val builderAssistant = MapperBuilderAssistant(configuration, resource)

        // parse inside sql element
        val sqlNodes = context.evalNodes("/mapper/sql")
        sqlNodes.forEach {
            var id = it.getStringAttribute("id")
            id = builderAssistant.applyCurrentNamespace(id, false)
            configuration.sqlFragments[id] = it
        }

        val list = context.evalNodes("select|insert|update|delete")

        // todo: check raw language is need ?
        // val lang = context.getStringAttribute("lang")

        val langDriver: LanguageDriver = XMLLanguageDriver()

        val entry = MybatisEntry(namespace)
        list.forEach {
            val methodName = it.getStringAttribute("id")
            // 1. enable include
            val includeParser = XMLIncludeTransformer(configuration, builderAssistant)
            includeParser.applyIncludes(it.node)

            // 2. follow parseStatementNode to remove all keys
            val selectKeyNodes = it.evalNodes("selectKey")
            selectKeyNodes.forEach { selectNode ->
                // todo: add key generator support
                val id: String = methodName + SelectKeyGenerator.SELECT_KEY_SUFFIX
                parseSelectKeyNode(id, selectNode, Any::class.java, langDriver, "", configuration, builderAssistant)
            }
            // remove before parser
            for (nodeToHandle in selectKeyNodes) {
                nodeToHandle.parent.node.removeChild(nodeToHandle.node)
            }

            val params: MutableMap<String, Any> = mutableMapOf()
            // 3. get rootNode to do some simple calculate
            val rootNode: MixedSqlNode = SimpleScriptBuilder(configuration, it).getNode()!!
            val dynamicContext = DynamicContext(configuration, params)
            // everyNode parse in here
            try {
                rootNode.apply(dynamicContext)
            } catch (e: Exception) {
                // ignore this exception log, because ognl will always run parserExpression
            }

            // 4. convert to source. it will replace all parameters => ?
            val sqlSourceParser = SqlSourceBuilder(configuration)
            val sqlSource2 = sqlSourceParser.parse(dynamicContext.sql, Any::class.java, dynamicContext.bindings)
            val boundSql = sqlSource2.getBoundSql(params)

            entry.methodSqlMap[methodName] = boundSql.sql
        }

        return entry
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
}
