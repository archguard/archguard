package com.thoughtworks.archguard.report.util

object NameUtil {

    fun getPackageName(name: String): String {
        if (!name.contains('.')) return ""

        val endIndex = name.indexOfLast { it == '.' }
        return name.substring(0, endIndex)
    }

    fun getClassName(name: String): String {
        if (!name.contains('.')) return name
        val endIndex = name.indexOfLast { it == '.' }
        return name.substring(endIndex + 1)
    }
}
