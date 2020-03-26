package com.thoughtworks.archguard.git.scanner.helper

import com.thoughtworks.archguard.git.scanner.Sql
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class Bean2Sql {
    fun bean2Sql(bean: Any): String {
        val clazz = bean::class

        val table = clazz.findAnnotation<Sql>()?.value ?: clazz.simpleName
        val columns = arrayListOf<String>()
        val values = arrayListOf<Any?>()
        clazz.memberProperties.forEach { property ->
            val column = property.findAnnotation<Sql>()?.value ?: property.name
            columns.add(column)
            values.add(property.call(bean))
        }
        val valueString = values.joinToString {
            if (it is String) {
                "'${it.replace("'", "''")}'"
            } else it.toString()
        }

        return "insert into $table(${columns.joinToString()}) values($valueString);"
    }
}