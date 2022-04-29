package org.archguard.analyser.sca.helper

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)

class Bean2Sql {
    private val STEP = 100

    fun bean2Sql(datas: List<Any>): String {
        val clazz = datas[0]::class

        val table = clazz.findAnnotation<Sql>()?.value ?: clazz.simpleName
        val columns = arrayListOf<String>()
        val values = arrayListOf<Any?>()

        clazz.memberProperties.forEach { property ->
            val column = property.findAnnotation<Sql>()?.value ?: property.name
            columns.add(column)
        }

        datas.map {
            clazz.memberProperties.forEach { property ->
                values.add(property.call(it))
            }
        }

        val chunkedByColumn = values.chunked(columns.size)
        val chunked = chunkedByColumn.chunked(STEP)
        return chunked.joinToString("\n") { list ->
            val valueString = list.joinToString(",") {
                val value = it.joinToString(transform = { item ->
                    if (item is String) {
                        "'${item.replace("'", "''")}'"
                    } else item.toString()
                })

                "($value)"
            }

            "insert into $table(${columns.joinToString()}) values $valueString;"
        }
    }
}