package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.scripting.xmltags.MixedSqlNode
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder
import org.apache.ibatis.session.Configuration

class SimpleXmlBuilder(configuration: Configuration, context: XNode) : XMLScriptBuilder(configuration, context) {
    fun rootNode(context: XNode): MixedSqlNode? {
        return parseDynamicTags(context)
    }
}