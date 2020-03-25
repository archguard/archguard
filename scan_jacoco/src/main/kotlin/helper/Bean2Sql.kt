package com.thoghtworks.archguard.scan_jacoco.helper

import com.thoghtworks.archguard.scan_jacoco.Sql
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.kotlinProperty


class Bean2Sql {
    fun bean2Sql(bean: Any): String {
        val clazz = bean.javaClass
        val table = clazz.getDeclaredAnnotation(Sql::class.java).value
        val columns = arrayListOf<String>()
        val values = arrayListOf<Any?>()
        clazz.declaredFields.forEach {
            val property = it.kotlinProperty!!
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