package org.archguard.scanner.analyser.database

fun sqlify(value: String): String {
    var text = handleRawString(value)
    text = removeBeginEndQuotes(text)

    // handle for variable
    text = removeVariableInLine(text)
    text = removeKotlinVariable(text)
    text = removeJdbiValueBind(text)
    text = removeEndWithMultipleSingleQuote(text)

    text = removeNextLine(text)
    // " " + module
    text = removePlusWithVariable(text)
    // " " + " "
    text = removePlusSymbol(text)
    text = processIn(text)

    // sql fixed
    text = fillPagination(text)
    return text
}

private fun fillPagination(text: String): String {
    var sqlText = text

    // fill lost offset offset
    sqlText = sqlText
        .replace("offset ''", "offset 10")
        .replace("OFFSET ''", "OFFSET 10")

    // fill lost limit variable
    sqlText = sqlText
        .replace("limit ''", "limit 10")
        .replace("LIMIT ''", "LIMIT 10")

    return sqlText
}

private val RAW_STRING_REGEX = "\"\"\"(((.*?)|(\r\n|\n))+)\"\"\"".toRegex()
private fun handleRawString(text: String): String {
    val rawString = RAW_STRING_REGEX.find(text) ?: return text

    return rawString.groups[1]!!.value
}

// some text: "\"+orderSqlPiece+\""
private val VARIABLE_IN_LINE = "(\"\\\\\"\\+[a-zA-Z0-9_]+\\+\"\\\\\")".toRegex()
private fun removeVariableInLine(text: String): String {
    val find = VARIABLE_IN_LINE.find(text) ?: return text

    return text.replace(VARIABLE_IN_LINE, "*")
}

private val IN_REGEX = "in\\s+\\((\\s+)?<([a-zA-Z0-9_]+)>(\\s+)?\\)".toRegex()
private fun processIn(text: String): String {
    val find = IN_REGEX.find(text) ?: return text

    return text.replace(IN_REGEX, "in (:${find.groups[2]!!.value})")
}

// example: `where system_id=:systemId ` => `where system_id=''`
private val JDBI_VALUE_BIND = ":([a-zA-Z0-9_]+)".toRegex()
private fun removeJdbiValueBind(text: String): String {
    val find = JDBI_VALUE_BIND.find(text) ?: return text
    return text.replace(JDBI_VALUE_BIND, "''")
}

private val KOTLIN_VARIABLE_WITH_QUOTE = "'\\\$([a-zA-Z0-9_]+)'".toRegex()
private val KOTLIN_VARIABLE = "\\\$([a-zA-Z0-9_]+)".toRegex()
private fun removeKotlinVariable(text: String): String {
    var str = text
    if (KOTLIN_VARIABLE_WITH_QUOTE.find(str) != null) {
        str = str.replace(KOTLIN_VARIABLE_WITH_QUOTE, "''")
    }

    if (KOTLIN_VARIABLE.find(str) != null) {
        str = str.replace(KOTLIN_VARIABLE, "''")
    }

    return str
}

private fun removeNextLine(text: String) = text
    .replace("\r\n", "")
    .replace("\n", "")

private fun removePlusSymbol(text: String) = text
    .replace("\"+\"", "")
    .replace("+\"", "")

private fun removePlusWithVariable(text: String) = text.replace("\"\\+([a-zA-Z0-9_]+)".toRegex(), "")
private fun removeEndWithMultipleSingleQuote(text: String) = text.replace("\'\'\\s+\'\'".toRegex(), "''")
private fun removeBeginEndQuotes(value: String): String {
    if (value.startsWith("\"") && value.endsWith("\"")) {
        return value.removeSuffix("\"").removePrefix("\"")
    }
    return value
}