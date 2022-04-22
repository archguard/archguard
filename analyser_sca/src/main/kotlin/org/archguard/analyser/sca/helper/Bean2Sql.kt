package org.archguard.analyser.sca.helper

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

@Sql("project_composition_dependencies")
data class CompositionDependency(
    @Sql("id") val id: String,
    @Sql("system_id") val systemid: String,
    @Sql("name") val name: String,
    @Sql("version") val version: String,
    @Sql("parent_id") val parentId: String,
    @Sql("dep_name") val dpeName: String,
    @Sql("dep_group") val depGroup: String,
    @Sql("dep_artifact") val depArtifact: String,
    @Sql("dep_metadata") val depMetadata: String,
    @Sql("dep_source") val depSource: String
)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)

class Bean2Sql {
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

        val valueString = values.joinToString {
            if (it is String) {
                "'${it.replace("'", "''")}'"
            } else it.toString()
        }

        return "insert into $table(${columns.joinToString()}) values($valueString);"
    }
}