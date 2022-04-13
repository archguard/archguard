package org.archguard.scanner.sourcecode.xml.mybatis

import org.apache.ibatis.parsing.TokenHandler

object handler : TokenHandler {
    override fun handleToken(content: String?): String {
        println(content)
        return ""
    }
}
